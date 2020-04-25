package com.dmytrod.cinemalist.data.repository

import androidx.paging.DataSource
import com.dmytrod.cinemalist.data.db.model.FavorableMovieModel
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel
import com.dmytrod.cinemalist.domain.entity.MovieEntity

interface IPersistenceRepository {
    fun getFavoriteMovies(): DataSource.Factory<Int, FavorableMovieModel>

    suspend fun storeList(list: List<MovieAPIModel>)

    suspend fun deleteMovieList()

    suspend fun toggleFavoriteMovie(movie: MovieEntity)
    fun getOngoingMovies(): DataSource.Factory<Int, FavorableMovieModel>
}