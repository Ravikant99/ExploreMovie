package com.ravi.exploremovie.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.ravi.exploremovie.R
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.screenRoutes.ScreenRoutes
import com.ravi.exploremovie.ui.composableItems.BottomNavigationBar

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    // Basic UI for now - to be implemented fully later
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Search Coming Soon",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F75FE))
        ) {
            Text("Back to Home")
        }
    }
    
    // Bottom Navigation
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
fun DefaultSearchView(
    navController: NavController,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    filteredMovies: List<MovieResult>,
) {
    // To be implemented later
}

@Composable
fun EmptySearchState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.spiderman),
            contentDescription = "No results found",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
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
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchResultItem(
    movieResult: MovieResult,
    onClick: () -> Unit,
) {
    // To be implemented later
}

// This will be our future SearchViewModel
class SearchViewModel : androidx.lifecycle.ViewModel() {
    // To be implemented later
}

