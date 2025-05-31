package com.ravi.exploremovie.ui.composableItems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravi.exploremovie.common.movie.model.MovieResult

@Composable
fun MovieRailSection(
    title: String,
    movies: List<MovieResult>,
    onSeeAllClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    itemContent: @Composable (MovieResult) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                "See All",
                color = Color(0xFF00FFFF),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickable { onSeeAllClick() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                itemContent(movie)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F1E)
@Composable
fun MovieRailSectionPreview() {
//    val mockMovies = listOf(
//        Result(
//            id = 1,
//            title = "Inception",
//            posterPath = null,
//            genreIds = listOf(28),
//            voteAverage = 4.5
//        ),
//        Result(
//            id = 2,
//            title = "Toy Story",
//            posterPath = null,
//            genreIds = listOf(35),
//            voteAverage = 4.3
//        ),
//        Result(
//            id = 3,
//            title = "The Conjuring",
//            posterPath = null,
//            genreIds = listOf(27),
//            voteAverage = 4.2
//        )
//    )

//    Box(modifier = Modifier.background(Color(0xFF0F0F1E))) {
//        MovieRailSection(
//            title = "Popular Now",
//            movies = getPopularMovies(),
//            onSeeAllClick = { /* No-op for preview */ }
//        ) { movie ->
//            MovieCard(movieResult = movie)
//        }
//    }
}
