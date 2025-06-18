package com.ravi.exploremovie.webServices

import com.ravi.exploremovie.common.movie.model.MovieGenreListResponse
import com.ravi.exploremovie.common.movie.model.MovieListResponse
import com.ravi.exploremovie.common.movie.model.PersonListResponse
import com.ravi.exploremovie.movieDetails.data.model.MovieDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Query("api_key") apiKey: String, 
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieListResponse

    @GET("trending/person/day")
    suspend fun getTrendingPerson(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): PersonListResponse

    @GET("movie/popular")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("api_key") apiKey: String): MovieGenreListResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String, 
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieListResponse
    
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int, 
        @Query("api_key") apiKey: String,
        @Query("append_to_response") appendToResponse: String = "credits"
    ): MovieDetails



}
