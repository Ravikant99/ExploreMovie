package com.ravi.exploremovie.ui.composableItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ravi.exploremovie.R
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.utils.ConstantUtils

@Composable
fun MovieRowItem(
    movieResult: MovieResult,
    onClick: () -> Unit = {},
) {
    val rating = (movieResult.voteAverage ?: 0.0) / 2 // Convert from 10-scale to 5-scale
    
    Column(
        modifier = Modifier
            .width(140.dp)
            .padding(end = 12.dp)
    ) {
        Card(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box {
                // Use Coil to load image
                val imageUrl = if (movieResult.posterPath != null) {
                    "${ConstantUtils.BASE_URL_IMAGE}${movieResult.posterPath}"
                } else {
                    null // will use the placeholder/error image
                }
                
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = movieResult.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.placeholder_movie),
                    error = painterResource(id = R.drawable.placeholder_movie)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", movieResult.voteAverage),
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = movieResult.title ?: "Unknown",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = movieResult.releaseDate?:"unknown",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieRowItem() {
//    val sampleMovie = Result(
//        id = 1,
//        title = "Avengers: Endgame",
//        posterPath = null,
//        genreIds = listOf(28),
//        voteAverage = 8.4
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF0F0F1E))
//            .padding(16.dp)
//    )
//    {
//        MovieCard(movieResult = sampleMovie)
//    }
}

