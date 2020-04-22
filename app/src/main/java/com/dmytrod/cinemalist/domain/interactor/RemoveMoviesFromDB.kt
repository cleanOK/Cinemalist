package com.dmytrod.cinemalist.domain.interactor

import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import kotlinx.coroutines.flow.flow

class RemoveMoviesFromDB(private val repository: PersistenceRepository) {
    fun execute() = flow<Any> {
        repository.deleteMovieList()
    }
}
