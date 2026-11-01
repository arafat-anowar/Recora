package com.veylqo.recora.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.veylqo.recora.ui.theme.CosmicBackground
import com.veylqo.recora.ui.theme.CosmicCardBorder
import com.veylqo.recora.ui.theme.CosmicGold
import com.veylqo.recora.ui.theme.CosmicGoldBorder
import com.veylqo.recora.ui.theme.CosmicGoldContainer
import com.veylqo.recora.ui.theme.CosmicPrimary
import com.veylqo.recora.ui.theme.CosmicSurface
import com.veylqo.recora.ui.theme.CosmicTextMuted
import com.veylqo.recora.ui.theme.CosmicTextPrimary
import com.veylqo.recora.ui.theme.CosmicTextSecondary

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var hapticFeedbackEnabled by remember { mutableStateOf(true) }
    var smoothAnimationsEnabled by remember { mutableStateOf(true) }
    var selectedPlan by remember { mutableStateOf("monthly") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "More",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
                Text(
                    text = "Membership & application preferences",
                    fontSize = 14.sp,
                    color = CosmicTextSecondary
                )
            }
        }

        // Section: Membership
        item {
            SectionHeader(title = "Membership")

            Spacer(modifier = Modifier.height(8.dp))

            // Premium Active Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicGoldContainer)
                    .border(1.dp, CosmicGoldBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "👑", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Recora Premium Active",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Monthly Subscription • Renews: Dec 01, 2026",
                            fontSize = 12.sp,
                            color = CosmicTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subscriptions 2-Column Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Monthly Plan
                SubscriptionCard(
                    title = "Monthly",
                    price = "$0.99",
                    period = "/mo",
                    subtitle = "Flexible subscription",
                    features = listOf("No watermark", "Auto instant save", "Cancel anytime"),
                    isSelected = selectedPlan == "monthly",
                    badge = null,
                    buttonLabel = if (selectedPlan == "monthly") "Active" else "Select",
                    onSelect = { selectedPlan = "monthly" },
                    modifier = Modifier.weight(1f)
                )

                // Lifetime Plan
                SubscriptionCard(
                    title = "Lifetime",
                    price = "$9.99",
                    period = " once",
                    subtitle = "Pay once, own forever",
                    features = listOf("No watermark", "4K & 2K unlock", "Lifetime pass"),
                    isSelected = selectedPlan == "lifetime",
                    badge = "BEST VALUE",
                    buttonLabel = if (selectedPlan == "lifetime") "Active" else "Select",
                    onSelect = { selectedPlan = "lifetime" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Restore & Redeem Code Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { Toast.makeText(context, "Purchases Restored", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CosmicTextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(CosmicCardBorder))
                ) {
                    Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Restore", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { Toast.makeText(context, "Redeem Code Prompt", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CosmicTextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(CosmicCardBorder))
                ) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Redeem Code", fontSize = 13.sp)
                }
            }
        }

        // Section: App Preferences
        item {
            SectionHeader(title = "App Preferences")

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurface)
                    .border(1.dp, CosmicCardBorder, RoundedCornerShape(20.dp))
                    .padding(vertical = 4.dp)
            ) {
                SwitchPreferenceRow(
                    title = "Haptic Feedback",
                    subtitle = "Vibrate on start, pause, and stop recording",
                    icon = Icons.Default.Vibration,
                    checked = hapticFeedbackEnabled,
                    onCheckedChange = { hapticFeedbackEnabled = it }
                )
                SwitchPreferenceRow(
                    title = "Smooth Animations",
                    subtitle = "Enable fluid cosmic motion and tab transitions",
                    icon = Icons.Default.Stars,
                    checked = smoothAnimationsEnabled,
                    onCheckedChange = { smoothAnimationsEnabled = it }
                )
                ActionPreferenceRow(
                    title = "Notification Settings",
                    subtitle = "Manage recording status & control bar alerts",
                    icon = Icons.Default.Notifications,
                    onClick = { Toast.makeText(context, "Notification Settings", Toast.LENGTH_SHORT).show() }
                )
            }
        }

        // Section: System & Hardware
        item {
            SectionHeader(title = "System & Hardware")

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurface)
                    .border(1.dp, CosmicCardBorder, RoundedCornerShape(20.dp))
                    .padding(vertical = 4.dp)
            ) {
                ActionPreferenceRow(
                    title = "Device Compatibility",
                    subtitle = "Inspect hardware encoder limits, resolutions & codecs",
                    icon = Icons.Default.CheckCircle,
                    iconTint = Color(0xFF4CAF50),
                    onClick = { Toast.makeText(context, "All Hardware Encoders OK", Toast.LENGTH_SHORT).show() }
                )
                ActionPreferenceRow(
                    title = "System Diagnostics & Health",
                    subtitle = "Audit storage headroom, battery policy & permissions",
                    icon = Icons.Default.HealthAndSafety,
                    iconTint = CosmicPrimary,
                    onClick = { Toast.makeText(context, "System Health 100%", Toast.LENGTH_SHORT).show() }
                )
                ActionPreferenceRow(
                    title = "Crash & Stability Journal",
                    subtitle = "Review locally recorded stability incidents & diagnostics",
                    icon = Icons.Default.BugReport,
                    iconTint = CosmicGold,
                    onClick = { Toast.makeText(context, "No Crashes Recorded", Toast.LENGTH_SHORT).show() }
                )
            }
        }

        // Section: About Recora
        item {
            SectionHeader(title = "About Recora")

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CosmicSurface)
                    .border(1.dp, CosmicCardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                InfoRow(label = "Version", value = "0.1 (Build 1)")
                InfoRow(label = "Package Name", value = "com.veylqo.recora")
                InfoRow(label = "Storage Directory", value = "Movies/Recora (Local)")

                Spacer(modifier = Modifier.height(12.dp))

                ActionLink(title = "Privacy Policy & Disclosures", subtitle = "Offline local storage guarantee & requested permissions")
                ActionLink(title = "Terms of Service", subtitle = "Standard usage policy & intellectual property rights")
                ActionLink(title = "Open-Source Licenses", subtitle = "Third-party libraries & software attributions")
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = CosmicTextPrimary,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
fun SubscriptionCard(
    title: String,
    price: String,
    period: String,
    subtitle: String,
    features: List<String>,
    isSelected: Boolean,
    badge: String?,
    buttonLabel: String,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CosmicSurface)
            .border(
                1.5.dp,
                if (isSelected) CosmicGold else CosmicCardBorder,
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Column {
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CosmicGoldContainer)
                        .border(1.dp, CosmicGoldBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicGold
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(18.dp))
            }

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CosmicTextPrimary
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = price,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
                Text(
                    text = period,
                    fontSize = 12.sp,
                    color = CosmicTextMuted
                )
            }

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = CosmicTextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = feature,
                        fontSize = 11.sp,
                        color = CosmicTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) CosmicSurface else CosmicPrimary
                )
            ) {
                Text(
                    text = buttonLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPrimary
                )
            }
        }
    }
}

@Composable
fun SwitchPreferenceRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CosmicBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = CosmicPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CosmicTextPrimary)
                Text(text = subtitle, fontSize = 11.sp, color = CosmicTextMuted)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CosmicTextPrimary,
                checkedTrackColor = CosmicPrimary,
                uncheckedThumbColor = CosmicTextMuted,
                uncheckedTrackColor = CosmicBackground
            )
        )
    }
}

@Composable
fun ActionPreferenceRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color = CosmicPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CosmicBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CosmicTextPrimary)
                Text(text = subtitle, fontSize = 11.sp, color = CosmicTextMuted)
            }
        }

        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CosmicTextMuted)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = CosmicTextMuted)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = CosmicTextPrimary)
    }
}

@Composable
fun ActionLink(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = CosmicTextPrimary)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CosmicTextMuted, modifier = Modifier.size(16.dp))
        }
        Text(text = subtitle, fontSize = 11.sp, color = CosmicTextMuted)
    }
}
