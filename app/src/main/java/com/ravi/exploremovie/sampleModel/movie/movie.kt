package com.ravi.exploremovie.sampleModel.movie

import com.ravi.exploremovie.R

data class Movie(
    val title: String,
    val genre: String,
    val rating: Float,
    val poster: Int = 0,
    val posterUrl: String? = null,
    val id: Int = 0
)

data class MovieBanner(
    val imageRes: Int,
    val title: String,
    val date: String
)

val banners = listOf(
    MovieBanner(R.drawable.black_panther, "Black Panther: Wakanda Forever", "On March 2, 2022"),
    MovieBanner(R.drawable.avenger, "Avengers: Multiverse", "Releasing April 12, 2022"),
    MovieBanner(R.drawable.black_panther, "Spider-Man: No Way Home", "Available Now")
)

fun getPopularMovies() = listOf(
    Movie("Spider-Man No Way Home", "Action", 4.5f, R.drawable.spiderman),
    Movie("Life of PI", "Action", 4.5f, R.drawable.spiderman),
    Movie("Riverdale", "Drama", 4.0f, R.drawable.spiderman),
    Movie("Spider-Man No Way Home", "Action", 4.5f, R.drawable.spiderman),
    Movie("Life of PI", "Action", 4.5f, R.drawable.spiderman),
    Movie("Riverdale", "Drama", 4.0f, R.drawable.spiderman)
)

fun getMoviesByCategory(category: String): List<Movie> {
    val allMovies = getPopularMovies() // Replace with actual data source
    return if (category == "All") allMovies
    else allMovies.filter { it.genre.equals(category, ignoreCase = true) }
}

fun getCuratedMovies(): List<Movie> {
    return getPopularMovies() // or getAllMovies() or a hardcoded sample
}

data class Actor(
    val name: String,
    val imageRes: Int // This should be a drawable resource ID
)

val actors = listOf(
    Actor("John Wilson", R.drawable.profile),
    Actor("John Deere", R.drawable.profile),
    Actor("John Cena", R.drawable.profile),
    Actor("John Stamp", R.drawable.profile),
    Actor("John Wilson", R.drawable.profile),
    Actor("John Deere", R.drawable.profile),
    Actor("John Cena", R.drawable.profile),
    Actor("John Stamp", R.drawable.profile)
)

