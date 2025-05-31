package com.ravi.exploremovie

import android.app.Application
import com.ravi.exploremovie.utils.HttpLoggingUtils

class ExploreMovieApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Enable HTTP logging at application startup
        HttpLoggingUtils.enableLogging()
    }
} 