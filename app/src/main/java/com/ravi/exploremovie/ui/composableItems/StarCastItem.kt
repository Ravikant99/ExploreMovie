package com.ravi.exploremovie.ui.composableItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ravi.exploremovie.R
import com.ravi.exploremovie.common.movie.model.PersonResult
import com.ravi.exploremovie.utils.ConstantUtils

@Composable
fun StarCastItem(
    personResult: PersonResult,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onItemClick)
            .padding(4.dp)
    ) {
        // Actor Image
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF1D1D35))
        ) {
            if (personResult.profilePath != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${ConstantUtils.IMAGE_URL}${personResult.profilePath}")
                        .crossfade(true)
                        .build(),
                    contentDescription = personResult.name ?: "Actor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.profile),
                    error = painterResource(id = R.drawable.profile)
                )
            } else {
                // Placeholder
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Actor Placeholder",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
        )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Actor Name
        Text(
            text = personResult.name ?: "Unknown Actor",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(90.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StarCastItemPreview() {
    Surface(
        color = Color(0xFF0F0F1E),
        modifier = Modifier.padding(16.dp)
    ) {
        StarCastItem(
            personResult = PersonResult(
                id = 1,
                name = "Tom Cruise",
                profilePath = null
            ),
            onItemClick = {}
        )
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFF0F0F1E, widthDp = 100, heightDp = 120)
//@Composable
//fun PreviewActorItem() {
//    val sampleActor = Actor(
//        name = "John Cena",
//        imageRes = R.drawable.profile // replace with a valid drawable resource
//    )
//
//    Box(modifier = Modifier.background(Color(0xFF0F0F1E))) {
//        ActorItem(actor = sampleActor)
//    }
//}