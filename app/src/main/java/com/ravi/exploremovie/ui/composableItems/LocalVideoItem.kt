package com.ravi.exploremovie.ui.composableItems

import android.media.browse.MediaBrowser
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ravi.exploremovie.common.localVideo.VideoItem

@Composable
fun LocalVideoItem(
    thumbnailUrl: String,
    title: String,
    duration: String,
    size: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2C38)
        )
        ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop)

            Spacer(modifier = Modifier.width(12.dp))

            Column (modifier = Modifier.weight(1f)){
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "|",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = size,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
        }
    }
}


@Composable
fun MediaItemCard(
    video: VideoItem,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2A2C38))
            .padding(8.dp)
    ) {
        // Thumbnail
        video.thumbnail?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = video.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } ?: Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column (modifier = Modifier.weight(1f)){
            Text(
                text = video.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDuration(video.duration),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "|",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${formatFileSize(video.size)}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }
    }
}

// Helpers
fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d:%02d", hours, minutes, seconds)
}

fun formatFileSize(size: Long): String {
    val kb = size / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1 -> String.format("%.2f GB", gb)
        mb >= 1 -> String.format("%.2f MB", mb)
        else -> String.format("%.2f KB", kb)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
fun MediaItemCardPreview() {
    MediaItemCard(
        video = VideoItem(
            uri = Uri.EMPTY,
            name = "Spider-Man No Way Home Peter Parker Home Return - 24 -05 - 2024 new.mp4",
            duration = 10_260_00L, // 2h 51m
            size = 1_910_000_000L, // ~1.78 GB
            thumbnail = null
        )
    )
}




//@Preview(showBackground = true)
//@Composable
//fun PreviewLocalVideoItem() {
//        LocalVideoItem(
//            thumbnailUrl = "https://image.tmdb.org/t/p/w500/scFDS0U5uYAjcVTyjNc7GmcZw1q.jpg",
//            title = "Spider-Man No Way Home Peter Parker- 24 -05 - 2024 new.mp4",
//            "2:51:05",
//            "1.78 GB")
//}

