package com.ravi.exploremovie.screens.player.exoplayer

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import android.net.Uri
import android.view.WindowManager

private const val TAG = "PlayerScreen"

@Composable
fun PlayerScreen(
    videoUri: Uri,
    videoTitle: String = "Video",
    videoList: List<Uri> = emptyList(),
    videoTitles: List<String> = emptyList(),
    currentIndex: Int = 0,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentTitle by viewModel.currentTitle.collectAsState()
    val showControls by viewModel.showControls.collectAsState()
    val isPlayerReady by viewModel.isPlayerReady.collectAsState()

    LaunchedEffect(Unit) {
        Log.d(TAG, "Initializing player with video: $videoTitle")
        viewModel.initializePlayer(
            videoUri = videoUri,
            videoTitle = videoTitle,
            videoList = videoList,
            videoTitles = videoTitles,
            currentIndex = currentIndex
        )
    }

    DisposableEffect(Unit) {
        Log.d(TAG, "Setting up screen configuration")
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            Log.d(TAG, "Cleaning up screen configuration")
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    Log.d(TAG, "ON_PAUSE - Pausing player")
                    viewModel.pausePlayer()
                }
                Lifecycle.Event.ON_RESUME -> {
                    Log.d(TAG, "ON_RESUME - Resuming player")
                    viewModel.resumePlayer()
                }
                Lifecycle.Event.ON_STOP -> {
                    Log.d(TAG, "ON_STOP - Stopping player")
                    viewModel.stopPlayer()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    Log.d(TAG, "ON_DESTROY - Player will be released by ViewModel")
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            Log.d(TAG, "Removing lifecycle observer")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (isPlayerReady) {
            PlayerView(
                viewModel = viewModel,
                videoList = videoList,
                activity = activity
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            TopControlsOverlay(
                title = currentTitle,
                onBackClick = {
                    Log.d(TAG, "Back button pressed")
                    onBack()
                }
            )
        }
    }
}

@Composable
private fun PlayerView(
    viewModel: PlayerViewModel,
    videoList: List<Uri>,
    activity: Activity
) {
    AndroidView(
        factory = { ctx ->
            Log.d(TAG, "Creating PlayerView")
            PlayerView(ctx).apply {
                player = viewModel.getPlayer()
                
                if (player == null) {
                    Log.e(TAG, "Player is null in PlayerView factory")
                    return@apply
                }
                useController = true
                controllerShowTimeoutMs = 3000
                controllerHideOnTouch = true

                val hasMultipleVideos = videoList.size > 1
                setShowNextButton(hasMultipleVideos)
                setShowPreviousButton(hasMultipleVideos)

                setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
                    viewModel.updateControlsVisibility(visibility == android.view.View.VISIBLE)
                })

                setFullscreenButtonClickListener { isFullScreen ->
                    Log.d(TAG, "Fullscreen toggled: $isFullScreen")
                    handleFullscreenToggle(activity, isFullScreen)
                }
            }
        },
        update = { playerView ->
            if (playerView.player == null) {
                Log.d(TAG, "Updating player reference in PlayerView")
                playerView.player = viewModel.getPlayer()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun TopControlsOverlay(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                )
            )
            .statusBarsPadding()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )
    }
}

private fun handleFullscreenToggle(activity: Activity, isFullScreen: Boolean) {
    try {
        if (isFullScreen) {
            Log.d(TAG, "Entering fullscreen mode")
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            val windowInsetsController =
                WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            windowInsetsController?.let {
                it.hide(WindowInsetsCompat.Type.systemBars())
                it.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            Log.d(TAG, "Exiting fullscreen mode")
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            val windowInsetsController =
                WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error toggling fullscreen", e)
    }
}
