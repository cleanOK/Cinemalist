package com.dmytrod.cinemalist.data.remote

import com.dmytrod.cinemalist.data.remote.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/now_playing")
    suspend fun getOngoingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MoviesResponse

}