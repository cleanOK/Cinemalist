package com.dmytrod.cinemalist.domain.interactor

import com.dmytrod.cinemalist.data.repository.RemoteRepository
import com.dmytrod.cinemalist.domain.entity.MovieEntity

class GetOngoingMovies(private val remoteRepository: RemoteRepository) {
    suspend fun execute(): List<MovieEntity> {
        return remoteRepository.getOngoingMovies().results.map(MovieEntity.fromRemote)
    }
}