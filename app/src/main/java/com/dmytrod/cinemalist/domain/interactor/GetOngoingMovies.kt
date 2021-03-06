package com.dmytrod.cinemalist.domain.interactor

import androidx.paging.DataSource
import com.dmytrod.cinemalist.data.repository.IPersistenceRepository
import com.dmytrod.cinemalist.domain.entity.MovieEntity

class GetOngoingMovies(
    private val persistenceRepository: IPersistenceRepository
) : DataSourceFactoryInteractor<MovieEntity> {

    override fun execute(): DataSource.Factory<Int, MovieEntity> =
        persistenceRepository.getOngoingMovies().map(MovieEntity.fromDB)

    companion object {
        const val PAGE_SIZE = 20
    }
}