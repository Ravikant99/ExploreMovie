package com.ravi.exploremovie.webServices

import android.util.Log
import com.google.gson.Gson
import com.ravi.exploremovie.utils.HttpLoggingUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APICallback<T>(
    private val requestCode: String,
    private val repositoryDataReceivedListener: RepositoryDataReceivedListener
) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        validateResponse(call, response, repositoryDataReceivedListener, requestCode)
    }

    override fun onFailure(call: Call<T>, throwable: Throwable) {
        validateError(throwable, repositoryDataReceivedListener, requestCode, call.request().url.toString())
    }

    private fun validateError(throwable: Throwable, repositoryDataReceivedListener: RepositoryDataReceivedListener, requestCode: String, url: String) {
        repositoryDataReceivedListener.onWebServiceFailed(
            message = throwable.message,
            requestCode = requestCode,
            responseCode = 99,
            url = url
        )
    }

    private fun validateResponse(call: Call<T>, response: Response<T>, repositoryDataReceivedListener: RepositoryDataReceivedListener, requestCode: String) {
        try {
            val responseBody = response.body()
            val errorBody = response.errorBody()?.string()
            val requestUrl = call.request().url.toString()
            
            if (response.isSuccessful) {
                if (responseBody != null) {
                    val responseString = Gson().toJson(responseBody)
                    repositoryDataReceivedListener.onWebServiceSuccess(
                        response = responseString,
                        requestCode = requestCode
                    )
                } else {
                    repositoryDataReceivedListener.onWebServiceSuccess(
                        response = "",
                        requestCode = requestCode
                    )
                }
            } else {
                repositoryDataReceivedListener.onWebServiceFailed(
                    message = errorBody.toString(),
                    responseCode = response.code(),
                    requestCode = requestCode,
                    url = requestUrl
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            repositoryDataReceivedListener.onWebServiceFailed(
                message = e.message,
                responseCode = 500,
                requestCode = requestCode,
                url = call.request().url.toString()
            )
        }
    }
}