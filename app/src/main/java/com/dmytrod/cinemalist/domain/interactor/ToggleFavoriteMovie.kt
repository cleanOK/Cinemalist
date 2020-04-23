package com.dmytrod.cinemalist.domain.interactor

import android.util.Log
import androidx.annotation.StringRes
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import kotlinx.coroutines.flow.flow

class ToggleFavoriteMovie(private val persistenceRepository: PersistenceRepository) {

    fun execute(movie: MovieEntity) = flow<Result> {
        try {
            persistenceRepository.toggleFavoriteMovie(movie)
            emit(Result.Success)
        } catch (e: Throwable) {
            Log.e("TEST", "failed to toggle movie ${movie.title}", e)
            val message = if (movie.isFavorite) R.string.failed_to_unfav else R.string.failed_to_fav
            emit(Result.Failure(message, e))
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failure(@StringRes val errorMessageRes: Int, val cause: Throwable) : Result()
    }
}
