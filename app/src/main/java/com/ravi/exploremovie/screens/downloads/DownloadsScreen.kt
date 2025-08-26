package com.ravi.exploremovie.screens.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.ravi.exploremovie.ui.composableItems.BottomNavigationBar
import com.ravi.exploremovie.ui.composableItems.LoaderView
import com.ravi.exploremovie.ui.composableItems.MediaItemCard
import com.ravi.exploremovie.ui.theme.DarkBackground
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun DownloadsScreen(
    navController: NavController,
    viewModel: DownloadsViewModel = viewModel()) {

    val context = LocalContext.current
    val videos by viewModel.videos.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    // Choose permission depending on Android version
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadVideos()
        } else {
            Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    // Request/check permission when screen opens
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.loadVideos()
            }
            else -> {
                launcher.launch(permission)
            }
        }
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
                    text = "Downloads",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Box(modifier = Modifier.size(40.dp))
            }

            when {
                isLoading -> {
                    LoaderView(message = "Fetching downloads...", tint = Color.White)
                }

                videos.isEmpty() -> {
                    EmptyContentMessage("No downloads found")
                }

                else -> {
                    LazyColumn {
                        items(videos) { video ->
                            MediaItemCard(video)
                        }

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
fun DownloadsPreview() {
    val navController = rememberNavController()
    DownloadsScreen(
        navController = navController,
    )
}

