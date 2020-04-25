package com.dmytrod.cinemalist.domain.interactor

import android.util.Log
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.data.repository.IPersistenceRepository
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import kotlinx.coroutines.flow.flow

class ToggleFavoriteMovie(private val persistenceRepository: IPersistenceRepository) :
    FlowInteractor<MovieEntity, Unit> {

    override fun execute(param: MovieEntity) = flow<Result<Unit>> {
        val result = try {
            persistenceRepository.toggleFavoriteMovie(param)
            Result.Success(Unit)
        } catch (e: Throwable) {
            Log.e("TEST", "failed to toggle movie ${param.title}", e)
            val message = if (param.isFavorite) R.string.failed_to_unfav else R.string.failed_to_fav
            Result.Failure<Unit>(message, e)
        }
        emit(result)
    }

}
