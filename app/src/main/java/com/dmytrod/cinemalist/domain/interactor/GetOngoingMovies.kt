package com.dmytrod.cinemalist.domain.interactor

import com.dmytrod.cinemalist.data.repository.RemoteRepository
import com.dmytrod.cinemalist.data.repository.ResponseHandler
import com.dmytrod.cinemalist.domain.OngoingMoviesState
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOngoingMovies(
    private val remoteRepository: RemoteRepository
) {
    fun execute(): Flow<OngoingMoviesState> = flow {
        emit(OngoingMoviesState.Loading)
        when (val response = remoteRepository.getOngoingMovies()) {
            is ResponseHandler.Response.Success -> {
                val results = response.data.results
                emit(
                    if (results.isEmpty()) {
                        OngoingMoviesState.Empty
                    } else {
                        OngoingMoviesState.Success(results.map(MovieEntity.fromRemote))
                    }
                )

            }
            is ResponseHandler.Response.Error -> {
                emit(OngoingMoviesState.Error(response.remoteError))
            }
        }
    }
}