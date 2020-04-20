package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.TMDBApiService

class RemoteRepository(private val service: TMDBApiService) {
    suspend fun getOngoingMovies() = service.getOngoingMovies(page = 1)
}