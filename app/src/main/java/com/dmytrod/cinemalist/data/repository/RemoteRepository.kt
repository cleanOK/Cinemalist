package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.TMDBApiService
import com.dmytrod.cinemalist.data.remote.model.MoviesResponse

class RemoteRepository(
    private val service: TMDBApiService,
    private val responseHandler: ResponseHandler
) {
    suspend fun getOngoingMovies(page: Int): ResponseHandler.Response<MoviesResponse> =
        try {
            responseHandler.handleSuccess(service.getOngoingMovies(page = page))
        } catch (e: Throwable) {
            responseHandler.handleException(e)
        }

}