package com.veylqo.recora.model

import android.net.Uri

data class RecordingItem(
    val id: Long,
    val uri: Uri,
    val name: String,
    val durationFormatted: String,
    val sizeFormatted: String,
    val dateFormatted: String,
    val qualityTag: String,
    val thumbnailUri: Uri? = null,
    val sizeBytes: Long = 0L
)

data class StorageStats(
    val recoraSizeBytes: Long = 0L,
    val freeSizeBytes: Long = 0L,
    val totalSizeBytes: Long = 0L,
    val clipCount: Int = 0
) {
    val recoraSizeFormatted: String
        get() = formatSize(recoraSizeBytes)
    val freeSizeFormatted: String
        get() = formatSize(freeSizeBytes)
    val totalSizeFormatted: String
        get() = formatSize(totalSizeBytes)

    val usedPercentage: Float
        get() = if (totalSizeBytes > 0) ((totalSizeBytes - freeSizeBytes).toFloat() / totalSizeBytes.toFloat()) else 0f
}

fun formatSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1.0 -> String.format("%.1f GB", gb)
        mb >= 1.0 -> String.format("%.1f MB", mb)
        kb >= 1.0 -> String.format("%.1f KB", kb)
        else -> "$bytes B"
    }
}
