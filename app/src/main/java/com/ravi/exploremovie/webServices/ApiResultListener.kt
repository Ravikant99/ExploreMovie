package com.ravi.exploremovie.webServices

sealed class ApiResultListener<out T> {
    data class Success<out T>(val data: T) : ApiResultListener<T>()
    data class Failure(val message: String?, val code: Int, val url: String) : ApiResultListener<Nothing>()
    class Loading<T>: ApiResultListener<T>()
}

//sealed class ApiResultListener<T>{
//    class Success<T>(val data:T): ApiResultListener<T>()
//    class Error<T>(val error:Throwable?,val data:T? = null): ApiResultListener<T>()
//    class Loading<T>: ApiResultListener<T>()
//}

// In your core/util/Resource.kt file
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}