package com.ravi.exploremovie.screens.player.exoplayer

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    
    companion object {
        private const val TAG = "PlayerViewModel"
    }
    private val _currentTitle = MutableStateFlow("Video")
    val currentTitle: StateFlow<String> = _currentTitle.asStateFlow()
    
    private val _showControls = MutableStateFlow(true)
    val showControls: StateFlow<Boolean> = _showControls.asStateFlow()
    
    private val _currentMediaIndex = MutableStateFlow(0)
    val currentMediaIndex: StateFlow<Int> = _currentMediaIndex.asStateFlow()
    
    private val _isPlayerReady = MutableStateFlow(false)
    val isPlayerReady: StateFlow<Boolean> = _isPlayerReady.asStateFlow()

    private var _exoPlayer: ExoPlayer? = null

    private var isInitialized = false

    @Synchronized
    fun getPlayer(): ExoPlayer? = _exoPlayer

    @Synchronized
    fun initializePlayer(
        videoUri: Uri,
        videoTitle: String,
        videoList: List<Uri>,
        videoTitles: List<String>,
        currentIndex: Int
    ) {
        if (isInitialized) {
            Log.d(TAG, "Player already initialized, skipping...")
            return
        }
        
        try {
            Log.d(TAG, "Initializing player...")

            if (_exoPlayer == null) {
                _exoPlayer = createExoPlayer()
            }
            
            _exoPlayer?.let { player ->

                _currentTitle.value = videoTitle
                _currentMediaIndex.value = currentIndex

                val mediaItems = if (videoList.isNotEmpty()) {
                    createPlaylist(videoList, videoTitles)
                } else {
                    listOf(createMediaItem(videoUri, videoTitle))
                }
                
                player.setMediaItems(mediaItems, currentIndex, 0)
                player.prepare()
                player.playWhenReady = true
                
                _isPlayerReady.value = true
                isInitialized = true
                
                Log.d(TAG, "Player initialized successfully with ${mediaItems.size} items")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing player", e)
            releasePlayer()
        }
    }

    private fun createExoPlayer(): ExoPlayer {
        return ExoPlayer.Builder(getApplication()).build().apply {

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build()
            setAudioAttributes(audioAttributes, true)

            repeatMode = Player.REPEAT_MODE_OFF

            addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    handleMediaItemTransition(mediaItem)
                }
                
                override fun onPlaybackStateChanged(playbackState: Int) {
                    Log.d(TAG, "Playback state changed: $playbackState")
                    when (playbackState) {
                        Player.STATE_ENDED -> Log.d(TAG, "Playback ended")
                        Player.STATE_READY -> Log.d(TAG, "Player ready")
                        Player.STATE_BUFFERING -> Log.d(TAG, "Buffering...")
                        Player.STATE_IDLE -> Log.d(TAG, "Player idle")
                    }
                }
                
                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    Log.e(TAG, "Player error: ${error.message}", error)
                }
            })
        }
    }

    private fun createPlaylist(videoList: List<Uri>, videoTitles: List<String>): List<MediaItem> {
        return videoList.mapIndexed { index, uri ->
            val title = if (videoTitles.isNotEmpty() && index < videoTitles.size) {
                videoTitles[index]
            } else {
                "Video ${index + 1}"
            }
            createMediaItem(uri, title)
        }
    }

    private fun createMediaItem(uri: Uri, title: String): MediaItem {
        return MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(
                androidx.media3.common.MediaMetadata.Builder()
                    .setTitle(title)
                    .build()
            )
            .build()
    }

    private fun handleMediaItemTransition(mediaItem: MediaItem?) {
        mediaItem?.mediaMetadata?.title?.toString()?.let { title ->
            _currentTitle.value = title
            Log.d(TAG, "Media transition to: $title")
        }
        _exoPlayer?.let {
            _currentMediaIndex.value = it.currentMediaItemIndex
        }
    }

    fun updateControlsVisibility(visible: Boolean) {
        _showControls.value = visible
    }

    @Synchronized
    fun pausePlayer() {
        try {
            _exoPlayer?.let { player ->
                if (player.isPlaying) {
                    player.playWhenReady = false
                    Log.d(TAG, "Player paused")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error pausing player", e)
        }
    }

    @Synchronized
    fun resumePlayer() {
        try {
            _exoPlayer?.let { player ->
                if (!player.isPlaying && isInitialized) {
                    player.playWhenReady = true
                    Log.d(TAG, "Player resumed")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error resuming player", e)
        }
    }

    @Synchronized
    fun stopPlayer() {
        try {
            _exoPlayer?.let { player ->
                player.pause()
                Log.d(TAG, "Player stopped")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping player", e)
        }
    }

    fun hasMultipleVideos(): Boolean {
        return try {
            (_exoPlayer?.mediaItemCount ?: 0) > 1
        } catch (e: Exception) {
            Log.e(TAG, "Error checking media count", e)
            false
        }
    }

    fun isPlaying(): Boolean {
        return try {
            _exoPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Error checking play state", e)
            false
        }
    }

    @Synchronized
    fun releasePlayer() {
        try {
            Log.d(TAG, "Releasing player...")
            _exoPlayer?.let { player ->
                player.stop()
                player.clearMediaItems()
                player.release()
            }
            _exoPlayer = null
            isInitialized = false
            _isPlayerReady.value = false
            Log.d(TAG, "Player released successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing player", e)
            _exoPlayer = null
            isInitialized = false
            _isPlayerReady.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared, releasing player")
        releasePlayer()
    }
}
