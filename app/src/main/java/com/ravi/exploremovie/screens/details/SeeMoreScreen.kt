package com.ravi.exploremovie.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.common.movie.model.PersonResult
import com.ravi.exploremovie.screenRoutes.ScreenRoutes
import com.ravi.exploremovie.ui.composableItems.BottomNavigationBar
import com.ravi.exploremovie.ui.composableItems.LoaderView
import com.ravi.exploremovie.ui.composableItems.MovieCardItem
import com.ravi.exploremovie.ui.composableItems.StarCastItem
import com.ravi.exploremovie.ui.theme.AccentOrange
import com.ravi.exploremovie.ui.theme.DarkBackground

@Composable
fun SeeMoreScreen(
    navController: NavController,
    contentType: String,
    title: String,
    viewModel: SeeMoreViewModel = viewModel()
) {
    val movies by viewModel.movies.collectAsState()
    val persons by viewModel.persons.collectAsState()
    val isInitialLoading by viewModel.isInitialLoading.collectAsState()
    val isPaginationLoading by viewModel.isPaginationLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    LaunchedEffect(contentType) {
        viewModel.fetchContentByType(contentType)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Box(modifier = Modifier.size(40.dp))
            }

            // Content
            when {
                isInitialLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoaderView(message = "Loading content...", tint = Color.White)
                    }
                }
                error != null && movies.isEmpty() && persons.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Error",
                                color = Color.Red,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error ?: "An unknown error occurred",
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.fetchContentByType(contentType) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                else -> {
                    when (contentType) {
                        "popular", "upcoming", "top_rated" -> {
                            if (movies.isNotEmpty()) {
                                MovieGridWithPagination(
                                    movies = movies,
                                    isPaginationLoading = isPaginationLoading,
                                    navController = navController,
                                    currentPage = currentPage,
                                    totalPages = totalPages,
                                    onLoadMore = { viewModel.loadNextPage() }
                                )
                            } else {
                                EmptyContentMessage("No movies found")
                            }
                        }
                        "trending_persons" -> {
                            if (persons.isNotEmpty()) {
                                PersonsGridWithPagination(
                                    persons = persons,
                                    isPaginationLoading = isPaginationLoading,
                                    currentPage = currentPage,
                                    totalPages = totalPages,
                                    onLoadMore = { viewModel.loadNextPage() }
                                )
                            } else {
                                EmptyContentMessage("No persons found")
                            }
                        }
                        else -> EmptyContentMessage("Unknown content type")
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

@Composable
private fun MovieGridWithPagination(
    movies: List<MovieResult>,
    isPaginationLoading: Boolean,
    navController: NavController,
    currentPage: Int,
    totalPages: Int,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItemIndex >= (totalItems - 4)
        }
        .collect { shouldLoadMore ->
            if (shouldLoadMore && 
                currentPage < totalPages && 
                !isPaginationLoading &&
                movies.isNotEmpty()) {
                onLoadMore()
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = if (isPaginationLoading) 60.dp else 12.dp // Extra space for loading indicator
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = movies.size,
                key = { index -> "${movies[index].id}_$index" } // Unique key using ID + index
            ) { index ->
                val movie = movies[index]
                MovieCardItem(
                    movie = movie,
                    onItemClick = {
                        movie.id?.let { movieId ->
                            navController.navigate("${ScreenRoutes.DetailsScreen.route}/$movieId")
                        }
                    }
                )
            }
        }
        
        // Show loading indicator at the bottom when loading more
        if (isPaginationLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center),
                    color = Color.White
                )
            }
        }
        
        // Show pagination info
        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color(0x80000000))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Page $currentPage of $totalPages",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun PersonsGridWithPagination(
    persons: List<PersonResult>,
    isPaginationLoading: Boolean,
    currentPage: Int,
    totalPages: Int,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItemIndex >= (totalItems - 6)
        }
        .collect { shouldLoadMore ->
            if (shouldLoadMore && 
                currentPage < totalPages && 
                !isPaginationLoading &&
                persons.isNotEmpty()) {
                onLoadMore()
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = if (isPaginationLoading) 60.dp else 12.dp // Extra space for loading indicator
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = persons.size,
                key = { index -> "${persons[index].id}_$index" } // Unique key using ID + index
            ) { index ->
                val person = persons[index]
                StarCastItem(
                    personResult = person,
                    onItemClick = { }
                )
            }
        }
        
        // Show loading indicator at the bottom when loading more
        if (isPaginationLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center),
                    color = Color.White
                )
            }
        }
        
        // Show pagination info
        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color(0x80000000))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Page $currentPage of $totalPages",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun EmptyContentMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeeMoreScreenPreview() {
    val navController = rememberNavController()
    SeeMoreScreen(
        navController = navController,
        contentType = "popular",
        title = "Popular Movies"
    )
}

