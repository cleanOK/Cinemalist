package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.remote.model.MoviesResponse

interface IRemoteRepository {
    suspend fun getOngoingMovies(page: Int): IResponseHandler.Response<MoviesResponse>
}