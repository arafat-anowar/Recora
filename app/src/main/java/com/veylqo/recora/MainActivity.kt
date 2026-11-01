package com.veylqo.recora

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.veylqo.recora.model.RecordingState
import com.veylqo.recora.service.ScreenRecorderService
import com.veylqo.recora.ui.MainViewModel
import com.veylqo.recora.ui.components.BottomCapsuleNavBar
import com.veylqo.recora.ui.components.RecoraTab
import com.veylqo.recora.ui.screens.MoreScreen
import com.veylqo.recora.ui.screens.RecordScreen
import com.veylqo.recora.ui.screens.RecordingsScreen
import com.veylqo.recora.ui.theme.RecoraTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var recorderService: ScreenRecorderService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ScreenRecorderService.LocalBinder
            recorderService = binder.getService()
            isBound = true

            // Observe service state
            recorderService?.let { s ->
                lifecycleOwnerScopeCollect {
                    s.recordingState.collectLatest { state ->
                        viewModel.setRecordingState(state)
                        if (state is RecordingState.Idle) {
                            viewModel.loadRecordings(this@MainActivity)
                        }
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            recorderService = null
            isBound = false
        }
    }

    private val screenCaptureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            startRecordingService(result.resultCode, result.data!!)
        } else {
            Toast.makeText(this, "Screen capture permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val micGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: true
        if (!micGranted) {
            Toast.makeText(this, "Audio permission denied. Recording will mute audio.", Toast.LENGTH_SHORT).show()
        }
        requestScreenCapture()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate()

        // Bind to recorder service if already running
        Intent(this, ScreenRecorderService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        setContent {
            RecoraTheme {
                val selectedTab by viewModel.selectedTab.collectAsState()
                val recordingState by viewModel.recordingState.collectAsState()
                val config by viewModel.recordingConfig.collectAsState()
                val recordingsList by viewModel.recordingsList.collectAsState()
                val storageStats by viewModel.storageStats.collectAsState()

                val context = this@MainActivity

                LaunchedEffect(Unit) {
                    viewModel.loadRecordings(context)
                }

                Scaffold(
                    bottomBar = {
                        BottomCapsuleNavBar(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTab) {
                            RecoraTab.RECORD -> {
                                RecordScreen(
                                    recordingState = recordingState,
                                    config = config,
                                    onStartRecordClicked = { checkPermissionsAndStart() },
                                    onStopRecordClicked = { stopRecordingService() },
                                    onConfigChanged = { viewModel.updateConfig(it) },
                                    onProClicked = { viewModel.selectTab(RecoraTab.MORE) }
                                )
                            }
                            RecoraTab.RECORDINGS -> {
                                RecordingsScreen(
                                    recordings = recordingsList,
                                    storageStats = storageStats,
                                    onDeleteRecording = { uri -> viewModel.deleteRecording(context, uri) }
                                )
                            }
                            RecoraTab.MORE -> {
                                MoreScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkPermissionsAndStart() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            requestScreenCapture()
        }
    }

    private fun requestScreenCapture() {
        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        screenCaptureLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

    private fun startRecordingService(resultCode: Int, resultData: Intent) {
        val config = viewModel.recordingConfig.value
        val metrics = resources.displayMetrics

        val serviceIntent = Intent(this, ScreenRecorderService::class.java).apply {
            action = ScreenRecorderService.ACTION_START
            putExtra(ScreenRecorderService.EXTRA_RESULT_CODE, resultCode)
            putExtra(ScreenRecorderService.EXTRA_RESULT_DATA, resultData)

            val (width, height) = when (config.orientationOption) {
                com.veylqo.recora.model.OrientationOption.PORTRAIT -> Pair(
                    minOf(metrics.widthPixels, metrics.heightPixels),
                    maxOf(metrics.widthPixels, metrics.heightPixels)
                )
                com.veylqo.recora.model.OrientationOption.LANDSCAPE -> Pair(
                    maxOf(metrics.widthPixels, metrics.heightPixels),
                    minOf(metrics.widthPixels, metrics.heightPixels)
                )
                com.veylqo.recora.model.OrientationOption.AUTO -> Pair(config.qualityOption.width, config.qualityOption.height)
            }

            putExtra(ScreenRecorderService.EXTRA_WIDTH, width)
            putExtra(ScreenRecorderService.EXTRA_HEIGHT, height)
            putExtra(ScreenRecorderService.EXTRA_FPS, config.qualityOption.fps)
            putExtra(ScreenRecorderService.EXTRA_BITRATE, config.qualityOption.bitrate)
            putExtra(ScreenRecorderService.EXTRA_AUDIO_OPTION, config.audioOption.name)
            putExtra(ScreenRecorderService.EXTRA_COUNTDOWN, config.countdownOption.seconds)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun stopRecordingService() {
        val stopIntent = Intent(this, ScreenRecorderService::class.java).apply {
            action = ScreenRecorderService.ACTION_STOP
        }
        startService(stopIntent)
    }

    private fun lifecycleOwnerScopeCollect(block: suspend () -> Unit) {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: androidx.lifecycle.LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_START) {
                    kotlinx.coroutines.GlobalScope.launch {
                        block()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}
