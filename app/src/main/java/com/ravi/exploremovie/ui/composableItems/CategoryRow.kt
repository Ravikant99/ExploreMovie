package com.ravi.exploremovie.ui.composableItems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryRow(
    title: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 12.dp)
        ) {
            items(items) { item ->
                val isSelected = selectedItem == item
                Text(
                    text = item,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(
                            if (isSelected) Color(0xFF00FFFF) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                        .clickable { onItemSelected(item) },
                    color = if (isSelected) Color.Black else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryRowPreview() {
    val categories = listOf("All", "Comedy", "Animation", "Documentary", "Sci-fi", "Horror", "Romance")
    var selectedCategory by remember { mutableStateOf("All") }

    Surface(color = Color(0xFF0F0F1E)) {
        CategoryRow(
            title = "Categories",
            items = categories,
            selectedItem = selectedCategory,
            onItemSelected = { selectedCategory = it }
        )
    }
}


