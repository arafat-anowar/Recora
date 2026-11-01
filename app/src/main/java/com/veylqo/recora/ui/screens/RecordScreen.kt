package com.veylqo.recora.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.veylqo.recora.model.AudioOption
import com.veylqo.recora.model.CountdownOption
import com.veylqo.recora.model.OrientationOption
import com.veylqo.recora.model.QualityOption
import com.veylqo.recora.model.RecordingConfig
import com.veylqo.recora.model.RecordingState
import com.veylqo.recora.ui.theme.CosmicBackground
import com.veylqo.recora.ui.theme.CosmicCardBorder
import com.veylqo.recora.ui.theme.CosmicGold
import com.veylqo.recora.ui.theme.CosmicGoldContainer
import com.veylqo.recora.ui.theme.CosmicPrimary
import com.veylqo.recora.ui.theme.CosmicRecordOuter
import com.veylqo.recora.ui.theme.CosmicRecordRed
import com.veylqo.recora.ui.theme.CosmicSurface
import com.veylqo.recora.ui.theme.CosmicTextMuted
import com.veylqo.recora.ui.theme.CosmicTextPrimary
import com.veylqo.recora.ui.theme.CosmicTextSecondary

@Composable
fun RecordScreen(
    recordingState: RecordingState,
    config: RecordingConfig,
    onStartRecordClicked: () -> Unit,
    onStopRecordClicked: () -> Unit,
    onConfigChanged: (RecordingConfig) -> Unit,
    onProClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Recora",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
                Text(
                    text = when (recordingState) {
                        is RecordingState.Recording -> "Recording in progress..."
                        is RecordingState.CountingDown -> "Starting in ${recordingState.secondsRemaining}s..."
                        is RecordingState.Paused -> "Recording paused"
                        else -> "Ready when you are"
                    },
                    fontSize = 14.sp,
                    color = CosmicTextSecondary
                )
            }

            // Pro Badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CosmicGoldContainer)
                    .border(1.dp, CosmicGold, CircleShape)
                    .clickable { onProClicked() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "👑", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PRO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Record Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val isRecording = recordingState is RecordingState.Recording

            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1.0f,
                targetValue = if (isRecording) 1.08f else 1.02f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(CosmicRecordOuter)
                        .clickable {
                            if (isRecording) onStopRecordClicked() else onStartRecordClicked()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // White inner ring
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        // Central Red Dot
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(if (isRecording) RoundedCornerShape(12.dp) else CircleShape)
                                .background(CosmicRecordRed)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = when (recordingState) {
                        is RecordingState.Recording -> "Tap to stop recording"
                        is RecordingState.CountingDown -> "Get ready..."
                        is RecordingState.Paused -> "Tap to resume"
                        else -> "Tap to record"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
            }
        }

        // Settings 2-Column Cards Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(300.dp)
        ) {
            item {
                SettingCard(
                    title = "Audio",
                    value = config.audioOption.label,
                    icon = Icons.Default.VolumeUp,
                    onClick = {
                        val next = when (config.audioOption) {
                            AudioOption.INTERNAL -> AudioOption.MIC
                            AudioOption.MIC -> AudioOption.MUTE
                            AudioOption.MUTE -> AudioOption.INTERNAL
                        }
                        onConfigChanged(config.copy(audioOption = next))
                    }
                )
            }
            item {
                SettingCard(
                    title = "Quality",
                    value = config.qualityOption.label,
                    icon = Icons.Default.DesktopWindows,
                    onClick = {
                        val next = when (config.qualityOption) {
                            QualityOption.UHD_4K -> QualityOption.QHD_2K
                            QualityOption.QHD_2K -> QualityOption.FHD_1080P
                            QualityOption.FHD_1080P -> QualityOption.HD_720P
                            QualityOption.HD_720P -> QualityOption.UHD_4K
                        }
                        onConfigChanged(config.copy(qualityOption = next))
                    }
                )
            }
            item {
                SettingCard(
                    title = "Orientation",
                    value = config.orientationOption.label,
                    icon = Icons.Default.Smartphone,
                    onClick = {
                        val next = when (config.orientationOption) {
                            OrientationOption.PORTRAIT -> OrientationOption.LANDSCAPE
                            OrientationOption.LANDSCAPE -> OrientationOption.AUTO
                            OrientationOption.AUTO -> OrientationOption.PORTRAIT
                        }
                        onConfigChanged(config.copy(orientationOption = next))
                    }
                )
            }
            item {
                SettingCard(
                    title = "Timer",
                    value = config.countdownOption.label,
                    icon = Icons.Default.Timer,
                    onClick = {
                        val next = when (config.countdownOption) {
                            CountdownOption.IMMEDIATE -> CountdownOption.SEC_3
                            CountdownOption.SEC_3 -> CountdownOption.SEC_5
                            CountdownOption.SEC_5 -> CountdownOption.SEC_10
                            CountdownOption.SEC_10 -> CountdownOption.IMMEDIATE
                        }
                        onConfigChanged(config.copy(countdownOption = next))
                    }
                )
            }
            item {
                ToggleSettingCard(
                    title = "Controls",
                    isEnabled = config.controlsEnabled,
                    icon = Icons.Default.PlayArrow,
                    onClick = { onConfigChanged(config.copy(controlsEnabled = !config.controlsEnabled)) }
                )
            }
            item {
                ToggleSettingCard(
                    title = "Pen & Tools",
                    isEnabled = config.penEnabled,
                    icon = Icons.Default.Create,
                    onClick = { onConfigChanged(config.copy(penEnabled = !config.penEnabled)) }
                )
            }
            item {
                ToggleSettingCard(
                    title = "Facecam",
                    isEnabled = config.facecamEnabled,
                    icon = Icons.Default.CameraAlt,
                    onClick = { onConfigChanged(config.copy(facecamEnabled = !config.facecamEnabled)) }
                )
            }
            item {
                ToggleSettingCard(
                    title = "Touches",
                    isEnabled = config.touchesEnabled,
                    icon = Icons.Default.TouchApp,
                    onClick = { onConfigChanged(config.copy(touchesEnabled = !config.touchesEnabled)) }
                )
            }
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CosmicSurface)
            .border(1.dp, CosmicCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CosmicBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = CosmicTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = CosmicTextMuted
                )
                Text(
                    text = value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
            }
        }
    }
}

@Composable
fun ToggleSettingCard(
    title: String,
    isEnabled: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CosmicSurface)
            .border(1.dp, CosmicCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CosmicBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isEnabled) CosmicPrimary else CosmicTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "$title: ${if (isEnabled) "ON" else "OFF"}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isEnabled) CosmicPrimary else CosmicTextPrimary
            )
        }
    }
}
