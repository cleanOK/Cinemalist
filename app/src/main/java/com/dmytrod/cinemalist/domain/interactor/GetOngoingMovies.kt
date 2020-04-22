package com.dmytrod.cinemalist.domain.interactor

import androidx.paging.DataSource
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import com.dmytrod.cinemalist.domain.entity.MovieEntity

class GetOngoingMovies(
    private val persistenceRepository: PersistenceRepository
) {

    fun execute(): DataSource.Factory<Int, MovieEntity> =
        persistenceRepository.getMovies().map(MovieEntity.fromDB)

    companion object {
        const val PAGE_SIZE = 20
    }
}