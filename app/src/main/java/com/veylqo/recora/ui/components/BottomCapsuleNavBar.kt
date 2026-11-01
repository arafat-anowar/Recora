package com.veylqo.recora.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.veylqo.recora.ui.theme.CosmicBackground
import com.veylqo.recora.ui.theme.CosmicPrimary
import com.veylqo.recora.ui.theme.CosmicSurface
import com.veylqo.recora.ui.theme.CosmicTextMuted
import com.veylqo.recora.ui.theme.CosmicTextPrimary

enum class RecoraTab(val label: String, val icon: ImageVector) {
    RECORD("Record", Icons.Default.Videocam),
    RECORDINGS("Recordings", Icons.Default.PlayCircle),
    MORE("More", Icons.Default.MoreHoriz)
}

@Composable
fun BottomCapsuleNavBar(
    selectedTab: RecoraTab,
    onTabSelected: (RecoraTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(CircleShape)
                .background(CosmicSurface)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RecoraTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) CosmicPrimary else Color.Transparent,
                    animationSpec = tween(durationMillis = 250),
                    label = "bgColor"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) CosmicTextPrimary else CosmicTextMuted,
                    animationSpec = tween(durationMillis = 250),
                    label = "textColor"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .clickable { onTabSelected(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = textColor
                        )
                        if (isSelected) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.label,
                                color = textColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
