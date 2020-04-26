package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.TMDBApiService
import com.dmytrod.cinemalist.data.remote.model.MoviesResponse
import timber.log.Timber

class RemoteRepository(
    private val service: TMDBApiService,
    private val apiKey: String,
    private val responseHandler: IResponseHandler
) : IRemoteRepository {
    override suspend fun getOngoingMovies(page: Int): IResponseHandler.Response<MoviesResponse> =
        try {
            responseHandler.handleSuccess(service.getOngoingMovies(page = page, apiKey = apiKey))
        } catch (e: Throwable) {
            Timber.e(e, "failed to load page $page")
            responseHandler.handleException(e)
        }

}