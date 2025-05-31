package com.ravi.exploremovie.common.movie.model

import com.google.gson.annotations.SerializedName

data class GenreResult(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null
)