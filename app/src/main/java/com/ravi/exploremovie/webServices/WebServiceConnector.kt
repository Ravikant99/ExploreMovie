package com.ravi.exploremovie.webServices

import android.util.Log
import com.google.gson.GsonBuilder
import com.ravi.exploremovie.common.movie.model.MovieGenreListResponse
import com.ravi.exploremovie.common.movie.model.MovieListResponse
import com.ravi.exploremovie.common.movie.model.PersonListResponse
import com.ravi.exploremovie.movieDetails.data.model.MovieDetails
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.ravi.exploremovie.utils.ConstantUtils
import com.ravi.exploremovie.utils.HttpLoggingUtils
import com.ravi.exploremovie.video.data.model.TrailerResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class WebServiceConnector private constructor() {
    private val TAG = "WebServiceConnector"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .apply {
                if (HttpLoggingUtils.isLoggingEnabled()) {
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ConstantUtils.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val apiService: ApiServices by lazy {
        retrofit.create(ApiServices::class.java)
    }

    companion object {
        val instance: WebServiceConnector by lazy {
            WebServiceConnector()
        }
    }

    suspend fun getTrendingPersons(page: Int = 1): PersonListResponse {
        Log.d(TAG, "Fetching trending persons, page: $page")
        return apiService.getTrendingPerson(ConstantUtils.getApiKey(), page)
    }

    suspend fun getDiscoverMovies(page: Int = 1): MovieListResponse {
        Log.d(TAG, "Fetching discover movies, page: $page")
        return apiService.getDiscoverMovies(ConstantUtils.getApiKey(), page)
    }

    suspend fun getPopularMovies(page: Int = 1): MovieListResponse {
        Log.d(TAG, "Fetching popular movies, page: $page")
        return apiService.getTrendingMovies(ConstantUtils.getApiKey(), page)
    }

    suspend fun getTopRatedMovies(page: Int = 1): MovieListResponse {
        Log.d(TAG, "Fetching top rated movies, page: $page")
        return apiService.getTopRatedMovies(ConstantUtils.getApiKey(), page)
    }

    suspend fun getUpcomingMovies(page: Int = 1): MovieListResponse {
        Log.d(TAG, "Fetching upcoming movies, page: $page")
        return apiService.getUpcomingMovies(ConstantUtils.getApiKey(), page)
    }

    suspend fun searchMovies(query: String, page: Int = 1): MovieListResponse {
        Log.d(TAG, "Searching movies with query: $query, page: $page")
        return apiService.searchMovies(ConstantUtils.getApiKey(), query, page)
    }

    suspend fun getMovieGenres(): MovieGenreListResponse {
        Log.d(TAG, "Fetching movie genres")
        return apiService.getMovieGenres(ConstantUtils.getApiKey())
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        Log.d(TAG, "Fetching movie details for ID: $movieId")
        return apiService.getMovieDetails(movieId, ConstantUtils.getApiKey())
    }

    suspend fun getMovieTrailer(movieId: Int): TrailerResponse {
        Log.d(TAG, "Fetching movie details for ID: $movieId")
        return apiService.getMovieTrailer(movieId,ConstantUtils.getApiKey())
    }
}
