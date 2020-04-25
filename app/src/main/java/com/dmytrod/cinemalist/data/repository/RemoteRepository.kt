package com.dmytrod.cinemalist.data.repository

import android.util.Log
import com.dmytrod.cinemalist.data.remote.TMDBApiService
import com.dmytrod.cinemalist.data.remote.model.MoviesResponse

class RemoteRepository(
    private val service: TMDBApiService,
    private val responseHandler: IResponseHandler
) : IRemoteRepository {
    override suspend fun getOngoingMovies(page: Int): IResponseHandler.Response<MoviesResponse> =
        try {
            responseHandler.handleSuccess(service.getOngoingMovies(page = page))
        } catch (e: Throwable) {
            Log.e("TEST", "failed to load page $page", e)
            responseHandler.handleException(e)
        }

}