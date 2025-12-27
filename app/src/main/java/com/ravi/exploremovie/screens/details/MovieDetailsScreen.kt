package com.ravi.exploremovie.screens.details

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.ravi.exploremovie.R
import com.ravi.exploremovie.movieDetails.data.model.Cast
import com.ravi.exploremovie.movieDetails.data.model.MovieDetails
import com.ravi.exploremovie.sampleModel.movie.Actor
import com.ravi.exploremovie.sampleModel.movie.actors
import com.ravi.exploremovie.ui.composableItems.AppIcons
import com.ravi.exploremovie.ui.composableItems.BottomNavigationBar
import com.ravi.exploremovie.ui.composableItems.CastMemberItem
import com.ravi.exploremovie.ui.composableItems.LoaderView
import com.ravi.exploremovie.ui.composableItems.ShimmerMovieDetailsScreen
import com.ravi.exploremovie.ui.theme.*
import com.ravi.exploremovie.utils.ConstantUtils
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.ravi.exploremovie.screenRoutes.ScreenRoutes
import com.ravi.exploremovie.webServices.Resource

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailsScreen(
    navController: NavController,
    movieId: Int = -1,
    viewModel: MovieDetailsViewModel = viewModel()
) {
    // State from ViewModel
    val popularMovies by viewModel.popularMovies.collectAsState()
    val currentMovieDetails by viewModel.currentMovieDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val trailerState by viewModel.trailerState.collectAsState()
    
    // Get context for opening YouTube
    val context = LocalContext.current

    // Fetch movie details if we have a valid ID
    LaunchedEffect(movieId) {
        if (movieId > 0) {
            viewModel.fetchMovieDetails(movieId)
            viewModel.fetchMovieTrailer(movieId)
        }
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        if (isLoading) {
            ShimmerMovieDetailsScreen()
        } else if (currentMovieDetails == null && error != null) {
            // Show error state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                    onClick = { 
                        if (movieId > 0) {
                            viewModel.fetchMovieDetails(movieId)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                ) {
                    Text("Retry")
                }
            }
        } else if (currentMovieDetails != null) {
            // Show movie details
            val movie = currentMovieDetails!!
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp) // Add padding for the bottom navigation bar
                    .verticalScroll(scrollState)
            ) {
                // Movie Poster as background with header elements
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(520.dp)
                ) {
                    // Background poster (zoomed out version)
                    val imageUrl = if (movie.backdropPath != null) {
                        "${ConstantUtils.BASE_URL_IMAGE}${movie.backdropPath}"
                    } else if (movie.posterPath != null) {
                        "${ConstantUtils.BASE_URL_IMAGE}${movie.posterPath}"
                    } else {
                        null
                    }

                    if (imageUrl != null) {
                        GlideImage(
                            model = imageUrl,
                            contentDescription = movie.title ?: "Movie poster",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = placeholder(R.drawable.placeholder_movie),
                            failure = placeholder(R.drawable.placeholder_movie)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder_movie),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Dark gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x88000000),
                                        Color(0xDD000000)
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )

                    // Top Bar with back button and favorite button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back button
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
                                tint = TextPrimary
                            )
                        }

                        // Title in the center
                        Text(
                            text = movie.title ?: "Unknown Movie",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )

                        // Favorite button
                        IconButton(
                            onClick = { /* Handle favorite */ },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x33000000))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = Color.Red
                            )
                        }
                    }
                    // Centered movie poster (foreground)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .height(250.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            if (movie.posterPath != null) {
                                val posterUrl = "${ConstantUtils.BASE_URL_IMAGE}${movie.posterPath}"
                                GlideImage(
                                    model = posterUrl,
                                    contentDescription = movie.title ?: "Movie poster",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    loading = placeholder(R.drawable.placeholder_movie),
                                    failure = placeholder(R.drawable.placeholder_movie)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.placeholder_movie),
                                    contentDescription = "Movie poster",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    Column (modifier = Modifier.align(alignment = Alignment.BottomCenter)){
                        // Rating
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = AppIcons.StarIcon,
                                contentDescription = "Rating",
                                tint = AccentOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%.1f", movie.voteAverage),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentOrange
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            MovieInfoItem(text = movie.releaseDate?.take(4) ?: "Unknown")
                            MovieInfoItem(text = formatRuntime(movie.runtime))
                            MovieInfoItem(text = movie.genres?.mapNotNull { it?.name }?.joinToString(", ") ?: "Unknown")
                        }
                        // Play, Download and Share buttons in the same row
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Download button
                            ActionButton(
                                icon = AppIcons.DownloadIcon,
                                onClick = { /* Handle download */ }
                            )

                            // Play button
                            Button(
                                onClick = {
                                    when (trailerState) {
                                        is Resource.Success -> {
                                            val trailerKey = viewModel.getFirstTrailerKey()
                                            if (trailerKey != null) {
                                                // Open directly in YouTube app or browser
                                                openVideoInYouTube(context, trailerKey)
                                            } else {
                                                // Show "No YouTube trailer available" message
                                                Toast.makeText(
                                                    context,
                                                    "No trailer available",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        is Resource.Error -> {
                                            // Show error message
                                            Toast.makeText(
                                                context,
                                                "Failed to load trailer",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        is Resource.Loading -> {
                                            // Optionally show loading state
                                            Toast.makeText(
                                                context,
                                                "Loading trailer...",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentOrange
                                )
                            ) {
                                Icon(
                                    imageVector = AppIcons.PlayIcon,
                                    contentDescription = "Play",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Play",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Share button
                            ActionButton(
                                icon = AppIcons.ShareIcon,
                                onClick = { /* Handle share */ }
                            )
                        }

                    }

                }

                // Movie Info Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    // Story Line
                    Text(
                        text = "Story Line",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Movie Description
                    Text(
                        text = movie.overview ?: "No description available.",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Cast section with proper handling of API data
                    val castMembers = movie.credits?.cast?.filterNotNull() ?: emptyList()

                    if (castMembers.isNotEmpty()) {
                        // Cast title
                        Text(
                            text = "Cast",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Cast horizontal scrollable row with actual API data
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(castMembers.take(10)) { castMember ->
                                CastMemberItem(cast = castMember) {
                                    // Handle cast member click if needed
                                }
                            }
                        }
                    } else {
                        // Fallback to sample data if no cast is available
                        Text(
                            text = "Cast",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(actors.take(6)) { actor ->
                                CastMemberItem(actor = actor)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            // Show a placeholder or default state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Movie not found",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                ) {
                    Text("Go Back")
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

fun formatRuntime(minutes: Int?): String {
    if (minutes == null) return "Unknown"
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return if (hours > 0) {
        "${hours}h ${remainingMinutes}m"
    } else {
        "${minutes}m"
    }
}

@Composable
fun MovieInfoItem(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(DarkAccent)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun openVideoInYouTube(context: android.content.Context, videoId: String) {
    try {
        // Try to open in YouTube app first
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        appIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(appIntent)
        Toast.makeText(context, "Opening in YouTube app...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
        webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(webIntent)
        Toast.makeText(context, "Opening in browser...", Toast.LENGTH_SHORT).show()
    }
}

