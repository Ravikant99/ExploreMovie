package com.ravi.exploremovie.screens.player.youtube

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YoutubePlayerScreen(
    videoId: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    // Force landscape orientation
    DisposableEffect(Unit) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    // Fullscreen handling
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                YouTubePlayerView(ctx).apply {
                    enableAutomaticInitialization = false

                    lifecycleOwner.lifecycle.addObserver(this)
                    
                    // Fullscreen handling
                    enterFullScreen()

                    val iFramePlayerOptions = IFramePlayerOptions.Builder()
                        .controls(1) // Show player controls
                        .rel(0) // Don't show related videos
                        .ivLoadPolicy(3) // Don't show video annotations
                        .ccLoadPolicy(0) // Don't show closed captions by default
                        .build()
                    val listener = object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            Log.d("YoutubePlayer", "Player ready, loading video: $videoId")
                            youTubePlayer.loadVideo(videoId, 0f)
                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerConstants.PlayerState
                        ) {
                            Log.d("YoutubePlayer", "State changed: $state")
                            if (state == PlayerConstants.PlayerState.ENDED) {
                                onBackPressed()
                            }
                        }
                        
                        override fun onError(
                            youTubePlayer: YouTubePlayer,
                            error: PlayerConstants.PlayerError
                        ) {
                            Log.e("YoutubePlayer", "Player error: $error")

                            when (error) {
                                PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER,
                                PlayerConstants.PlayerError.VIDEO_NOT_FOUND,
                                PlayerConstants.PlayerError.INVALID_PARAMETER_IN_REQUEST -> {
                                    // Try to open in YouTube app or browser
                                    openVideoInYouTube(ctx, videoId)
                                    
                                    Toast.makeText(
                                        ctx,
                                        "Opening in YouTube...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    
                                    // Go back after opening YouTube
                                    activity.runOnUiThread {
                                        onBackPressed()
                                    }
                                }
                                else -> {
                                    // Other errors - just log them
                                    Log.e("YoutubePlayer", "Unhandled error: $error")
                                }
                            }
                        }
                    }
                    
                    initialize(listener, iFramePlayerOptions)
                }
            },
            onRelease = { playerView ->
                // Clean up when the view is removed
                playerView.release()
            },
            modifier = Modifier.fillMaxSize()
        )

        // Back button
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

private fun openVideoInYouTube(context: android.content.Context, videoId: String) {
    try {
        // Try to open in YouTube app first
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        appIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(appIntent)
    } catch (e: Exception) {
        // If YouTube app is not installed, open in browser
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
        webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(webIntent)
    }
}

// Extension function for fullscreen
fun YouTubePlayerView.enterFullScreen() {
    layoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}