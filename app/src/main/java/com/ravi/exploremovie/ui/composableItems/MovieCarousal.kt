package com.ravi.exploremovie.ui.composableItems

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ravi.exploremovie.R
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.sampleModel.movie.MovieBanner
import com.ravi.exploremovie.utils.ConstantUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieCarousal(
    banners: List<MovieResult>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { banners.size },
    onBannerClick: (MovieResult) -> Unit = {}
) {
    // Auto-scroll logic
//    LaunchedEffect(key1 = pagerState.currentPage, key2 = banners.size) {
//        if (banners.isNotEmpty()) {
//            delay(10_000L)
//            val nextPage = (pagerState.currentPage + 1) % banners.size
//            pagerState.animateScrollToPage(nextPage)
//        }
//    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        pageSpacing = 16.dp
    ) { page ->
        val isSelected = pagerState.currentPage == page
        val scale = if (isSelected) 1f else 0.9f
        val alpha = if (isSelected) 1f else 0.5f

        // 🔧 Wrap each item in Box to center it
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MovieBannerCard(
                movieResult = banners[page],
                scale = scale,
                alpha = alpha,
                onClick = { onBannerClick(banners[page]) }
            )
        }
    }

}

@Composable
fun MovieBannerCard(
    movieResult: MovieResult, 
    scale: Float, 
    alpha: Float,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.alpha = alpha
            }
            .width(340.dp)
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(id = R.drawable.spiderman),
                error = painterResource(id = R.drawable.spiderman)
            )
            // Bottom gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent),
                            startY = Float.POSITIVE_INFINITY,
                            endY = 0f
                        )
                    )
            )

            // Title and date
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = movieResult.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Release On ${movieResult.releaseDate}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewMovieBannerCard() {
//    val sampleBanner = MovieBanner(
//        imageRes = R.drawable.black_panther, // Make sure this drawable exists in your project
//        title = "Black Panther: Wakanda Forever",
//        date = "March 2, 2022"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.DarkGray),
//        contentAlignment = Alignment.Center
//    ) {
//        MovieBannerCard(
//            movieResult = ,
//            scale = 1f,
//            alpha = 1f
//        )
//    }
//}

