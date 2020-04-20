package com.dmytrod.cinemalist.data.remote

import com.dmytrod.cinemalist.data.remote.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/now_playing")
    suspend fun getOngoingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): MoviesResponse

    companion object {
        const val API_KEY = "989dd0a72eef8a89fb30f6027eb83fc7"
    }
}