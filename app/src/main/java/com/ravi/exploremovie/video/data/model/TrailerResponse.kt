package com.ravi.exploremovie.video.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrailerResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("results")
    val results: List<Result?>? = null
)