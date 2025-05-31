package com.ravi.exploremovie.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Interceptor to log OkHttp requests and responses
 */
class LoggingInterceptor : Interceptor {
    private val TAG = "okhttp"
    private val UTF8 = StandardCharsets.UTF_8

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Just proceed with the request and let OkHttp's HttpLoggingInterceptor 
        // handle the logging for us
        return chain.proceed(request)
    }
} 