package com.ravi.exploremovie.movieDetails.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class MovieCast(
    @SerialName("cast")
    val cast: List<CastX>? = listOf(),
    @SerialName("crew")
    val crew: List<CrewX>? = listOf(),
    @SerialName("id")
    val id: Int? = 0
)