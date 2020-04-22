package com.dmytrod.cinemalist.domain.interactor

import androidx.annotation.StringRes
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.data.db.model.MovieDBModel
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import com.dmytrod.cinemalist.data.repository.RemoteRepository
import com.dmytrod.cinemalist.data.repository.ResponseHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interactor for loading ongoing movies from TMDB api and saving results in DB
 */
class FetchMoviesByPage(
    private val remoteRepository: RemoteRepository,
    private val persistenceRepository: PersistenceRepository
) {
    fun execute(page: Int): Flow<Result> = flow {
        when (val response = remoteRepository.getOngoingMovies(page)) {
            is ResponseHandler.Response.Success -> {
                val results = response.data.results
                if (results.isEmpty()) {
                    emit(Result.Success(0, 0))
                    return@flow
                }
                try {
                    persistenceRepository.storeList(results.map(MovieDBModel.fromRemote))
                    emit(Result.Success(response.data.totalPages, response.data.page))
                } catch (e: Exception) {
                    emit(Result.Failure(R.string.failed_to_store_to_db))
                }
            }
            is ResponseHandler.Response.Error -> {
                emit(Result.Failure(response.remoteError.errorMessageRes))
            }
        }
    }

    sealed class Result {
        data class Success(val totalPages: Int, val page: Int) : Result()
        data class Failure(@StringRes val errorMessageRes: Int) : Result()
    }
}