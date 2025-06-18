package com.ravi.exploremovie.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ravi.exploremovie.R
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.screenRoutes.ScreenRoutes
import com.ravi.exploremovie.screens.home.HomeRepository
import com.ravi.exploremovie.ui.composableItems.BottomNavigationBar
import com.ravi.exploremovie.ui.composableItems.MovieCardItem
import com.ravi.exploremovie.utils.ConstantUtils

@Composable
fun SearchScreen(
    navController: NavController
) {
    // State collection
    val viewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory(HomeRepository()))
    val query by viewModel.query.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F1E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Search Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 20.dp, 4.dp, 0.dp)
            ) {
                SearchBar(
                    query = query,
                    onQueryChange = { viewModel.updateQuery(it) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Content Area
            Box(modifier = Modifier.weight(1f)) {
                when {
                    isLoading -> {
                        LoadingIndicator(modifier = Modifier.fillMaxSize())
                    }
                    error != null -> {
                        ErrorState(
                            error = error!!,
                            onRetry = { viewModel.performSearch(query) }
                        )
                    }
                    searchResults.isEmpty() && query.isNotEmpty() -> {
                        EmptySearchState(query = query)
                    }
                    query.isEmpty() -> {  // New case for empty query
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .padding(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.search), // Your image resource
                                contentDescription = "Search placeholder",
                                modifier = Modifier
                                    .size(250.dp)
                                    .padding(bottom = 16.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "Search for movies, TV shows, and more",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    else -> {
                        SearchResultsList(
                            movies = searchResults,
                            onMovieClick = { movie ->
                                navController.navigate(
                                    "${ScreenRoutes.DetailsScreen.route}/${movie.id}"
                                )
                            }
                        )
                    }
                }
            }
        }

        // Bottom navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(navController)
        }
    }
}

// Error State Component
    @Composable
private fun ErrorState(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error occurred",
            color = Color.Red,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Retry")
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = TextStyle(color = Color.White),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text("Search a title..", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        if (query.isNotEmpty()) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Clear",
                tint = Color.White,
                modifier = Modifier
                    .clickable { onQueryChange("") }
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) MaterialTheme.colorScheme.primary else DarkGray,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = category,
            color = if (isSelected) Color.Black else Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
private fun EmptySearchState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_search_results_found), // Replace with your actual drawable
            contentDescription = "No results found",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "No results for \"$query\"",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Try checking your spelling or use more general terms",
            color = LightGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchResultsList(
    movies: List<MovieResult>,
    onMovieClick: (MovieResult) -> Unit,
    modifier: Modifier = Modifier
) {

            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
//                    state = gridState,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(movies) { movie ->
                        MovieCardItem(
                            movie = movie,
                            onItemClick = {
                                onMovieClick(movie)
                            }
                        )
                    }
                }

//                // Show loading indicator at the bottom when loading more
//                if (isPaginationLoading) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .align(Alignment.BottomCenter)
//                    ) {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .size(36.dp)
//                                .align(Alignment.Center),
//                            color = Color.White
//                        )
//                    }
//                }
//
//                // Show pagination info
//                Box(
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .align(Alignment.TopEnd)
//                        .clip(CircleShape)
//                        .background(Color(0x80000000))
//                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                ) {
//                    Text(
//                        text = "Page $currentPage of $totalPages",
//                        color = Color.White,
//                        fontSize = 12.sp
//                    )
//                }
            }
        }
//    }
//}

@Composable
private fun SearchResultItem(
    movie: MovieResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGray)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Movie Poster
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(ConstantUtils.BASE_URL_IMAGE + movie.posterPath)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.spiderman),
                error = painterResource(R.drawable.profile)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Movie Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.releaseDate?.take(4) ?: "N/A",
                    color = LightGray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Icon(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "%.1f".format(movie.voteAverage ?: 0f),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}