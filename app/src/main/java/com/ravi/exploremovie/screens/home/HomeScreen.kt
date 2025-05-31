package com.ravi.exploremovie.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ravi.exploremovie.R
import com.ravi.exploremovie.common.movie.model.GenreResult
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.common.movie.model.PersonResult
import com.ravi.exploremovie.common.movie.model.MovieListResponse
import com.ravi.exploremovie.common.movie.model.PersonListResponse
import com.ravi.exploremovie.common.movie.model.MovieGenreListResponse
import com.ravi.exploremovie.screenRoutes.ScreenRoutes
import com.ravi.exploremovie.ui.composableItems.BottomNavigationBar
import com.ravi.exploremovie.ui.composableItems.CommonHorizontalRow
import com.ravi.exploremovie.ui.composableItems.GenreCategoryRow
import com.ravi.exploremovie.ui.composableItems.LoaderView
import com.ravi.exploremovie.ui.composableItems.MovieCardItem
import com.ravi.exploremovie.ui.composableItems.MovieCarousal
import com.ravi.exploremovie.ui.composableItems.PageIndicatorDots
import com.ravi.exploremovie.ui.composableItems.StarCastItem

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val categories =
        listOf("All", "Comedy", "Animation", "Documentary", "Sci-fi", "Horror", "Romance")
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedGenreId by remember { mutableStateOf<Int?>(null) }

    // State to track when retry is in progress
    var isRetrying by remember { mutableStateOf(false) }

    val discoverMovies by viewModel.discoverMovies.observeAsState(null)
    val popularMovies by viewModel.popularMovies.observeAsState(null)
    val topRatedMovies by viewModel.topRatedMovies.observeAsState(null)
    val upcomingMovies by viewModel.upcomingMovies.observeAsState(null)
    val trendingPerson by viewModel.trendingPersons.observeAsState(null)
    val movieGenres by viewModel.movieGenres.observeAsState(null)

    // Track overall loading state
    val isLoading = (discoverMovies == null || 
                   popularMovies == null || 
                   trendingPerson == null || 
                   movieGenres == null || 
                   upcomingMovies == null) || isRetrying

    // Fetch data once when the screen appears
    LaunchedEffect(Unit) {
        viewModel.fetchDiscoverMovies()
        viewModel.fetchPopularMovies()
        viewModel.fetchTopRatedMovies()
        viewModel.fetchUpcomingMovies()
        viewModel.fetchTrendingPersons()
        viewModel.fetchMovieGenres()
    }
    
    // Reset retrying state after data is loaded
    LaunchedEffect(discoverMovies, popularMovies, topRatedMovies, upcomingMovies, trendingPerson, movieGenres) {
        if (isRetrying && 
            discoverMovies != null && 
            popularMovies != null && 
            topRatedMovies != null && 
            upcomingMovies != null && 
            trendingPerson != null && 
            movieGenres != null) {
            isRetrying = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F1E))
                .padding(bottom = 64.dp) // Leave space for BottomNav
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                IconButton(onClick = {}) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        "Hello, Smith",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Let's stream your favorite movie",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { navController.navigate("searchScreen") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search a title..", color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.Tune, contentDescription = "Filter", tint = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                // Use the common LoaderView component
                LoaderView(
                    message = if (isRetrying) "Refreshing content..." else "Loading movies...",
                    tint = Color.White
                )
            } else {
                // Check if there are any errors
                val hasError = discoverMovies?.isFailure == true || 
                              popularMovies?.isFailure == true || 
                              trendingPerson?.isFailure == true || 
                              movieGenres?.isFailure == true || 
                              upcomingMovies?.isFailure == true

                if (hasError) {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.profile), // Replace with error icon
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Oops! Something went wrong",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Please check your internet connection and try again",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    // Set retrying state to true to show loader
                                    isRetrying = true
                                    
                                    // Fetch all data again
                                    viewModel.fetchDiscoverMovies()
                                    viewModel.fetchPopularMovies()
                                    viewModel.fetchTopRatedMovies()
                                    viewModel.fetchUpcomingMovies()
                                    viewModel.fetchTrendingPersons()
                                    viewModel.fetchMovieGenres()
                                    
                                    // Reset retrying state after a short delay
                                    // We'll use a coroutine in a separate LaunchedEffect
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1F75FE)
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                } else {
                    // Content loaded successfully - show scrollable content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Movie Carousel
                        val movies = discoverMovies?.getOrNull()?.results ?: emptyList()
                        if (movies.isNotEmpty()) {
                            val pagerState = rememberPagerState { movies.size }

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    MovieCarousal(
                                        banners = movies,
                                        pagerState = pagerState,
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        onBannerClick = { movie ->
                                            movie.id.let { movieId ->
                                                navController.navigate("${ScreenRoutes.DetailsScreen.route}/$movieId")
                                            }
                                        }
                                    )
                                    PageIndicatorDots(
                                        totalDots = movies.size,
                                        selectedIndex = pagerState.currentPage,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Movie Genres Category Row
                        val genres = movieGenres?.getOrNull()?.genres?.filterNotNull() ?: emptyList()
                        if (genres.isNotEmpty()) {
                            GenreCategoryRow(
                                title = "Movie Genres",
                                genres = genres,
                                selectedGenreId = selectedGenreId,
                                onGenreSelected = { genre ->
                                    selectedGenreId =
                                        if (selectedGenreId == genre.id) null else genre.id
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Popular Movies Row
                        val popularMoviesList = popularMovies?.getOrNull()?.results ?: emptyList()
                        if (popularMoviesList.isNotEmpty()) {
                            CommonHorizontalRow(
                                title = "Popular",
                                items = popularMoviesList,
                                onSeeAllClicked = {
                                    navController.navigate(
                                        "${ScreenRoutes.SeeMoreScreen.route}/popular/Popular%20Movies"
                                    )
                                },
                                itemContent = { movie ->
                                    MovieCardItem(movie = movie, onItemClick = {
                                        // Navigate to movie details
                                        movie.id.let { movieId ->
                                            navController.navigate("${ScreenRoutes.DetailsScreen.route}/$movieId")
                                        }
                                    })
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Trending Actors
                        val persons = trendingPerson?.getOrNull()?.results ?: emptyList()
                        if (persons.isNotEmpty()) {
                            CommonHorizontalRow(
                                title = "Trending Actors",
                                items = persons,
                                onSeeAllClicked = {
                                    navController.navigate(
                                        "${ScreenRoutes.SeeMoreScreen.route}/trending_persons/Trending%20Actors"
                                    )
                                },
                                itemContent = { person ->
                                    StarCastItem(personResult = person, onItemClick = {
                                        // Navigate to actor details
                                    })
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Upcoming Movies
                        val upcomingMoviesList = upcomingMovies?.getOrNull()?.results ?: emptyList()
                        if (upcomingMoviesList.isNotEmpty()) {
                            CommonHorizontalRow(
                                title = "Upcoming Movies",
                                items = upcomingMoviesList,
                                onSeeAllClicked = {
                                    navController.navigate(
                                        "${ScreenRoutes.SeeMoreScreen.route}/upcoming/Upcoming%20Movies"
                                    )
                                },
                                itemContent = { movie ->
                                    MovieCardItem(movie = movie, onItemClick = {
                                        // Navigate to movie details
                                        movie.id.let { movieId ->
                                            navController.navigate("${ScreenRoutes.DetailsScreen.route}/$movieId")
                                        }
                                    })
                                }
                            )
                        }

                        // Top Rated Movies
                        val topRatedMoviesList = topRatedMovies?.getOrNull()?.results ?: emptyList()
                        if (topRatedMoviesList.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            CommonHorizontalRow(
                                title = "Top Rated",
                                items = topRatedMoviesList,
                                onSeeAllClicked = {
                                    navController.navigate(
                                        "${ScreenRoutes.SeeMoreScreen.route}/top_rated/Top%20Rated%20Movies"
                                    )
                                },
                                itemContent = { movie ->
                                    MovieCardItem(movie = movie, onItemClick = {
                                        // Navigate to movie details
                                        movie.id.let { movieId ->
                                            navController.navigate("${ScreenRoutes.DetailsScreen.route}/$movieId")
                                        }
                                    })
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // Fixed BottomNavigationBar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    // Provide a dummy NavController for preview purposes
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
