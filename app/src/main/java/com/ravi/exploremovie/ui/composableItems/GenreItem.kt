package com.ravi.exploremovie.ui.composableItems

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravi.exploremovie.common.movie.model.GenreResult

@Composable
fun GenreItem(
    genre: GenreResult,
    isSelected: Boolean,
    onItemClicked: (GenreResult) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) Color(0xFF00FFFF) else Color(0xFF1D1D35)
            )
            .clickable { onItemClicked(genre) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre.name ?: "",
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun GenreCategoryRow(
    title: String,
    genres: List<GenreResult>,
    selectedGenreId: Int?,
    onGenreSelected: (GenreResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Title
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Genre items
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(genres) { genre ->
                GenreItem(
                    genre = genre,
                    isSelected = genre.id == selectedGenreId,
                    onItemClicked = onGenreSelected
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenreCategoryRowPreview() {
    val genres = listOf(
        GenreResult(id = 28, name = "Action"),
        GenreResult(id = 12, name = "Adventure"),
        GenreResult(id = 16, name = "Animation"),
        GenreResult(id = 35, name = "Comedy"),
        GenreResult(id = 80, name = "Crime")
    )
    
    var selectedGenreId by remember { mutableStateOf<Int?>(28) }
    
    Surface(color = Color(0xFF0F0F1E)) {
        GenreCategoryRow(
            title = "Movie Genres",
            genres = genres,
            selectedGenreId = selectedGenreId,
            onGenreSelected = { selectedGenreId = it.id }
        )
    }
} 