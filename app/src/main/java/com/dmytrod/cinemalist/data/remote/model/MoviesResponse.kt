package com.dmytrod.cinemalist.data.remote.model
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieAPIModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

