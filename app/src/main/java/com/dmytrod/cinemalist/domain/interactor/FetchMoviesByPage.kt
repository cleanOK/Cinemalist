package com.dmytrod.cinemalist.domain.interactor

import com.dmytrod.cinemalist.data.db.model.MovieDBModel
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import com.dmytrod.cinemalist.data.repository.RemoteRepository
import com.dmytrod.cinemalist.data.repository.ResponseHandler
import com.dmytrod.cinemalist.domain.OngoingMoviesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interactor for loading ongoing movies from TMDB api and saving results in DB
 */
class FetchMoviesByPage(
    private val remoteRepository: RemoteRepository,
    private val persistenceRepository: PersistenceRepository
) {
    fun execute(page: Int): Flow<OngoingMoviesState> = flow {
        emit(OngoingMoviesState.Loading)
        when (val response = remoteRepository.getOngoingMovies(page)) {
            is ResponseHandler.Response.Success -> {
                val results = response.data.results
                emit(
                    if (results.isEmpty()) {
                        OngoingMoviesState.Empty
                    } else {
                        persistenceRepository.storeList(results.map(MovieDBModel.fromRemote))
                        OngoingMoviesState.Success(response.data.totalPages)
                    }
                )
            }
            is ResponseHandler.Response.Error -> {
                emit(OngoingMoviesState.Error(response.remoteError))
            }
        }
    }
}