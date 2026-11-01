package com.veylqo.recora.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import com.veylqo.recora.model.RecordingItem
import com.veylqo.recora.model.StorageStats
import com.veylqo.recora.model.formatSize
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MediaStoreHelper {

    const val DIRECTORY_NAME = "Movies/Recora"

    fun createVideoContentValues(fileName: String): ContentValues {
        return ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, DIRECTORY_NAME)
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }
        }
    }

    fun queryRecordings(context: Context): List<RecordingItem> {
        val list = mutableListOf<RecordingItem>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.HEIGHT
        )

        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?"
        } else {
            "${MediaStore.Video.Media.DATA} LIKE ?"
        }
        val selectionArgs = arrayOf("%Recora%")
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        try {
            context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)

                val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn) ?: "Recora_video.mp4"
                    val durationMs = cursor.getLong(durationColumn)
                    val sizeBytes = cursor.getLong(sizeColumn)
                    val dateAddedSec = cursor.getLong(dateColumn)
                    val height = cursor.getInt(heightColumn)

                    val uri = ContentUris.withAppendedId(collection, id)
                    val durationFormatted = formatDuration(durationMs)
                    val sizeFormatted = formatSize(sizeBytes)
                    val dateFormatted = dateFormat.format(Date(dateAddedSec * 1000))
                    val qualityTag = getQualityTag(height)

                    list.add(
                        RecordingItem(
                            id = id,
                            uri = uri,
                            name = name,
                            durationFormatted = durationFormatted,
                            sizeFormatted = sizeFormatted,
                            dateFormatted = dateFormatted,
                            qualityTag = qualityTag,
                            thumbnailUri = uri,
                            sizeBytes = sizeBytes
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

    fun getStorageStats(context: Context, recordings: List<RecordingItem>): StorageStats {
        val totalRecoraBytes = recordings.sumOf { it.sizeBytes }

        return try {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            val availableBlocks = stat.availableBlocksLong

            val totalBytes = totalBlocks * blockSize
            val freeBytes = availableBlocks * blockSize

            StorageStats(
                recoraSizeBytes = totalRecoraBytes,
                freeSizeBytes = freeBytes,
                totalSizeBytes = totalBytes,
                clipCount = recordings.size
            )
        } catch (e: Exception) {
            StorageStats(
                recoraSizeBytes = totalRecoraBytes,
                freeSizeBytes = 100_000_000_000L,
                totalSizeBytes = 256_000_000_000L,
                clipCount = recordings.size
            )
        }
    }

    fun deleteRecording(context: Context, uri: Uri): Boolean {
        return try {
            val rows = context.contentResolver.delete(uri, null, null)
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getQualityTag(height: Int): String {
        return when {
            height >= 2160 -> "4K"
            height >= 1440 -> "2K"
            height >= 1080 -> "FHD"
            height >= 720 -> "HD"
            else -> "FHD"
        }
    }
}
