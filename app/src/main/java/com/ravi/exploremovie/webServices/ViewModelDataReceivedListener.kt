package com.ravi.exploremovie.webServices

interface ViewModelDataReceivedListener {
    fun dataReceivedInRepositorySuccess(jsonResponse: String, requestCode: String)
    fun dataReceivedInRepositoryFailed(message: String?, requestCode: String, responseCode: Int)
}
