package com.ravi.exploremovie.utils

open class ConstantUtils {
    companion object {

        const val API_KEY = "f92e0058e9f7d16bc7cfff76fd40a1bc" // tmdb api key
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"


        fun getApiKey() : String {
            return API_KEY
        }

        fun getBaseUrlImage() : String {
            return BASE_URL_IMAGE
        }

        fun getBaseUrl() : String {
            return BASE_URL
        }
    }
}