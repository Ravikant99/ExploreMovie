package com.ravi.exploremovie.common.movie.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class MovieGenreListResponse(
    @SerializedName("genres")
    val genres: List<GenreResult?>? = null
)