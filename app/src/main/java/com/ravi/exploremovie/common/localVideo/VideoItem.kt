package com.ravi.exploremovie.common.localVideo

import android.graphics.Bitmap
import android.net.Uri

data class VideoItem(
    val uri: Uri,
    val name: String,
    val duration: Long,
    val size: Long,
    val thumbnail: Bitmap?
)

