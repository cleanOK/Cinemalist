package com.dmytrod.cinemalist.domain.interactor

import androidx.paging.DataSource
import com.dmytrod.cinemalist.data.repository.IPersistenceRepository
import com.dmytrod.cinemalist.domain.entity.MovieEntity

class GetFavoriteMovies(
    private val persistenceRepository: IPersistenceRepository
) : DataSourceFactoryInteractor<MovieEntity> {

    override fun execute(): DataSource.Factory<Int, MovieEntity> =
        persistenceRepository.getFavoriteMovies().map(MovieEntity.fromDB)
}