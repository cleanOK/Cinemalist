package com.dmytrod.cinemalist.domain.interactor

import androidx.annotation.StringRes
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import kotlinx.coroutines.flow.flow

class RemoveMoviesFromDB(private val repository: PersistenceRepository) {
    fun execute() = flow<Result> {
        try {
            repository.deleteMovieList()
            emit(Result.Success)
        } catch (e: Throwable) {
            emit(Result.Failure(R.string.failed_to_clean_db, e))
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failure(@StringRes val errorMessage: Int, val cause: Throwable) : Result()
    }
}
