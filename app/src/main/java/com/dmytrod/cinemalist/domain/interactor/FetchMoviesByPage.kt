package com.dmytrod.cinemalist.domain.interactor

import androidx.annotation.StringRes
import com.dmytrod.cinemalist.R
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
        val result = when (val response = remoteRepository.getOngoingMovies(page)) {
            is ResponseHandler.Response.Success -> {
                val data = response.data
                try {
                    persistenceRepository.storeList(data.results)
                    Result.Success(data.totalPages, data.page)
                } catch (e: Throwable) {
                    Result.Failure(R.string.failed_to_store_to_db, e)
                }
            }
            is ResponseHandler.Response.Error -> {
                Result.Failure(response.remoteError.errorMessageRes, response.remoteError.cause)
            }
        }
        emit(result)
    }

    sealed class Result {
        data class Success(val totalPages: Int, val page: Int) : Result()
        data class Failure(@StringRes val errorMessageRes: Int, val e: Throwable) : Result()
    }
}