package com.ravi.exploremovie.screens.home

import android.util.Log
import com.ravi.exploremovie.common.movie.model.MovieGenreListResponse
import com.ravi.exploremovie.common.movie.model.PersonListResponse

import com.ravi.exploremovie.webServices.WebServiceConnector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.ravi.exploremovie.common.movie.model.MovieListResponse
import com.ravi.exploremovie.movieDetails.data.model.MovieDetails
import retrofit2.HttpException
import java.io.IOException

class HomeRepository {

    private val webServiceConnector = WebServiceConnector.instance
    private val TAG = "HomeRepository"

    suspend fun getTrendingPersons(page: Int = 1): Result<PersonListResponse> = safeApiCall {
        withContext(Dispatchers.IO) {
            webServiceConnector.getTrendingPersons(page)
        }
    }

    suspend fun getDiscoverMovies(page: Int = 1): Result<MovieListResponse> = safeApiCall {
        withContext(Dispatchers.IO) {
            webServiceConnector.getDiscoverMovies(page)
        }
    }

    suspend fun getPopularMovies(page: Int = 1): Result<MovieListResponse> = safeApiCall {
        withContext(Dispatchers.IO) {
            webServiceConnector.getPopularMovies(page)
        }
    }

    suspend fun getTopRatedMovies(page: Int = 1): Result<MovieListResponse> = safeApiCall {
        withContext(Dispatchers.IO) {
            webServiceConnector.getTopRatedMovies(page)
        }
    }

    suspend fun getUpcomingMovies(page: Int = 1): Result<MovieListResponse> = safeApiCall {
        withContext(Dispatchers.IO) {
            webServiceConnector.getUpcomingMovies(page)
        }
    }

    suspend fun getMovieGenres(): Result<MovieGenreListResponse> = safeApiCall {
        withContext(Dispatchers.IO) {
            webServiceConnector.getMovieGenres()
        }
    }

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> = safeApiCall {
        withContext(Dispatchers.IO) {
            webServiceConnector.getMovieDetails(movieId)
        }
    }

    private suspend inline fun <T> safeApiCall(crossinline apiCall: suspend () -> T): Result<T> {
        return try {
            val response = apiCall()
            Result.success(response)
        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            Result.failure(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error ${e.code()}: ${e.message()}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error", e)
            Result.failure(e)
        }
    }

    private fun <T> retrofit2.Response<T>.bodyOrThrow(): T {
        if (isSuccessful && body() != null) return body()!!
        throw HttpException(this)
    }
}
