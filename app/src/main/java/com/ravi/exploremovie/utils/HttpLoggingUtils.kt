package com.ravi.exploremovie.utils

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Utility class to manage HTTP logging globally
 */
object HttpLoggingUtils {
    private var isLoggingEnabled = false
    private const val TAG = "okhttp"
    
    /**
     * Enable OkHttp logging - call this at app startup
     */
    fun enableLogging() {
        isLoggingEnabled = true
        Log.i(TAG, "HTTP logging enabled")
    }
    
    /**
     * Disable OkHttp logging
     */
    fun disableLogging() {
        isLoggingEnabled = false
        Log.i(TAG, "HTTP logging disabled")
    }
    
    /**
     * Check if logging is currently enabled
     */
    fun isLoggingEnabled(): Boolean {
        return isLoggingEnabled
    }
    
    /**
     * Apply logging interceptor to an existing OkHttpClient builder
     */
    fun applyLogging(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        if (isLoggingEnabled) {
            // Add OkHttp's built-in logging interceptor
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder
    }
    
    /**
     * Create a new OkHttpClient with logging interceptor
     */
    fun createLoggingClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (isLoggingEnabled) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }
} 