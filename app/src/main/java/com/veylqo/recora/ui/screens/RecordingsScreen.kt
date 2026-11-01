package com.veylqo.recora.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.veylqo.recora.model.RecordingItem
import com.veylqo.recora.model.StorageStats
import com.veylqo.recora.ui.theme.CosmicBackground
import com.veylqo.recora.ui.theme.CosmicBadgeTag
import com.veylqo.recora.ui.theme.CosmicCardBorder
import com.veylqo.recora.ui.theme.CosmicPrimary
import com.veylqo.recora.ui.theme.CosmicSurface
import com.veylqo.recora.ui.theme.CosmicTextMuted
import com.veylqo.recora.ui.theme.CosmicTextPrimary
import com.veylqo.recora.ui.theme.CosmicTextSecondary

@Composable
fun RecordingsScreen(
    recordings: List<RecordingItem>,
    storageStats: StorageStats,
    onDeleteRecording: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                .padding(top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Recordings",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
                Text(
                    text = "Saved in Movies/Recora",
                    fontSize = 14.sp,
                    color = CosmicTextSecondary
                )
            }

            // Clip count badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CosmicBadgeTag)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${storageStats.clipCount} clips",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Storage Intelligence Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CosmicSurface)
                .border(1.dp, CosmicCardBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CosmicBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = "Storage",
                                tint = CosmicPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Storage Intelligence",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextPrimary
                        )
                    }

                    Text(
                        text = "Recora: ${storageStats.recoraSizeFormatted}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CosmicPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { storageStats.usedPercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = CosmicPrimary,
                    trackColor = CosmicBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Free: ${storageStats.freeSizeFormatted}",
                        fontSize = 12.sp,
                        color = CosmicTextMuted
                    )
                    Text(
                        text = "Capacity: ${storageStats.totalSizeFormatted}",
                        fontSize = 12.sp,
                        color = CosmicTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recordings List
        if (recordings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No recordings yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = CosmicTextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap the Record tab to capture your screen",
                        fontSize = 13.sp,
                        color = CosmicTextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(recordings, key = { it.id }) { recording ->
                    RecordingCardItem(
                        recording = recording,
                        onPlayClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(recording.uri, "video/mp4")
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        onShareClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "video/mp4"
                                putExtra(Intent.EXTRA_STREAM, recording.uri)
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Recording"))
                        },
                        onDeleteClick = { onDeleteRecording(recording.uri) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecordingCardItem(
    recording: RecordingItem,
    onPlayClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CosmicSurface)
            .border(1.dp, CosmicCardBorder, RoundedCornerShape(18.dp))
            .clickable { onPlayClick() }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Thumbnail / Play Preview Box
            Box(
                modifier = Modifier
                    .size(width = 64.dp, height = 48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CosmicBackground),
                contentAlignment = Alignment.Center
            ) {
                if (recording.thumbnailUri != null) {
                    AsyncImage(
                        model = recording.thumbnailUri,
                        contentDescription = "Thumbnail",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = CosmicPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recording.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${recording.durationFormatted} • ${recording.sizeFormatted} • ${recording.dateFormatted}",
                        fontSize = 12.sp,
                        color = CosmicTextMuted
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CosmicBadgeTag)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = recording.qualityTag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicPrimary
                        )
                    }
                }
            }

            // Three dots Overflow Menu
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = CosmicTextMuted
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(CosmicSurface)
                ) {
                    DropdownMenuItem(
                        text = { Text("Play", color = CosmicTextPrimary) },
                        leadingIcon = { Icon(Icons.Default.PlayArrow, contentDescription = null, tint = CosmicTextPrimary) },
                        onClick = {
                            showMenu = false
                            onPlayClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share", color = CosmicTextPrimary) },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, tint = CosmicTextPrimary) },
                        onClick = {
                            showMenu = false
                            onShareClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = Color.Red) },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                        onClick = {
                            showMenu = false
                            onDeleteClick()
                        }
                    )
                }
            }
        }
    }
}
