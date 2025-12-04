package com.ravi.exploremovie.screens.downloads

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.exploremovie.common.localVideo.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadsViewModel(application: Application) : AndroidViewModel(application) {

    private val _videos = MutableLiveData<List<VideoItem>>()
    val videos: LiveData<List<VideoItem>> = _videos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _hasPermission = MutableLiveData(true) // default assume true
    val hasPermission: LiveData<Boolean> = _hasPermission

    // Track if videos have been loaded to avoid unnecessary reloading
    private var videosLoaded = false

    fun checkPermission(context: Context) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        _hasPermission.value = granted
    }

    fun loadVideos(forceReload: Boolean = false) {
        // Skip loading if already loaded and not forcing reload
        if (videosLoaded && !forceReload) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)  // show loader
            val videoList = getAllVideos(getApplication<Application>().applicationContext)
            _videos.postValue(videoList)
            _isLoading.postValue(false) // hide loader
            videosLoaded = true
        }
    }

    fun refreshVideos() {
        loadVideos(forceReload = true)
    }
}


@SuppressLint("Range")
fun getAllVideos(context: Context): List<VideoItem> {
    val videoList = mutableListOf<VideoItem>()
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE
    )

    val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

    val cursor = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val name = it.getString(nameColumn)
            val duration = it.getLong(durationColumn)
            val size = it.getLong(sizeColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id
            )

            // Thumbnail
            val thumbnail = try {
                MediaStore.Video.Thumbnails.getThumbnail(
                    context.contentResolver,
                    id,
                    MediaStore.Video.Thumbnails.MINI_KIND,
                    null
                )
            } catch (e: Exception) {
                null
            }

            videoList.add(VideoItem(contentUri, name, duration, size, thumbnail))
        }
    }
    return videoList
}

