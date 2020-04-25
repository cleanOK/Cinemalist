package com.dmytrod.cinemalist.domain.interactor

import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.data.repository.IPersistenceRepository
import kotlinx.coroutines.flow.flow

class RemoveMoviesFromDB(private val repository: IPersistenceRepository) :
    FlowInteractor<Unit, Unit> {
    override fun execute(param: Unit) = flow {
        val result = try {
            repository.deleteMovieList()
            Result.Success(Unit)
        } catch (e: Throwable) {
            Result.Failure<Unit>(R.string.failed_to_clean_db, e)
        }
        emit(result)
    }
}
