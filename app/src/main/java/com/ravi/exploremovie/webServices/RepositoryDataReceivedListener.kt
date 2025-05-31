package com.ravi.exploremovie.webServices

interface RepositoryDataReceivedListener {

    fun onWebServiceSuccess(response: String?, requestCode: String)

    fun onWebServiceFailed(message: String?, responseCode: Int, requestCode: String, url: String)

}