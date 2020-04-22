package com.dmytrod.cinemalist.domain.interactor

import androidx.paging.DataSource
import com.dmytrod.cinemalist.data.repository.PersistenceRepository
import com.dmytrod.cinemalist.data.repository.RemoteRepository
import com.dmytrod.cinemalist.data.repository.ResponseHandler
import com.dmytrod.cinemalist.domain.OngoingMoviesState
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOngoingMovies(
    private val remoteRepository: RemoteRepository,
    private val persistenceRepository: PersistenceRepository
) {

    fun executeFromDB(): DataSource.Factory<Int, MovieEntity> =
        persistenceRepository.getMovies().map(MovieEntity.fromDB)

    companion object {
        const val PAGE_SIZE = 20
    }
}