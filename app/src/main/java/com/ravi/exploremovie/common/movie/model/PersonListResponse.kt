package com.ravi.exploremovie.common.movie.model

import com.google.gson.annotations.SerializedName

data class PersonListResponse(
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("results")
    val results: List<PersonResult> = emptyList(),
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0
)