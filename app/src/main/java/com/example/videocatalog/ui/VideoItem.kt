package com.example.videocatalog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.videocatalog.VideoInfo
import java.util.Locale

@Composable
fun VideoItem(info: VideoInfo, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(info.fileName, style = MaterialTheme.typography.titleMedium)
        if (info.error != null) {
            Text(info.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(
                "${formatDuration(info.durationUs)}  •  ${info.width}×${info.height}  •  ${info.videoMime ?: "unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                if (info.hasAudio) "Audio: yes" else "Audio: no",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    HorizontalDivider()
}

private fun formatDuration(durationUs: Long): String {
    if (durationUs < 0) return "--:--"
    val totalSeconds = durationUs / 1_000_000
    return String.format(Locale.US, "%02d:%02d", totalSeconds / 60, totalSeconds % 60)
}
