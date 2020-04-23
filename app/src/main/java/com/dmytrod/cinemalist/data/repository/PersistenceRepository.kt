package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.db.MovieDatabase
import com.dmytrod.cinemalist.data.db.model.FavoriteDBModel
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel
import com.dmytrod.cinemalist.domain.entity.MovieEntity

class PersistenceRepository(private val movieDatabase: MovieDatabase) {
    fun getMovies() = movieDatabase.movieDao().getFavorableMovies()

    suspend fun storeList(list: List<MovieAPIModel>) {
        movieDatabase.movieDao().insertOrUpdateFromApi(list)
        movieDatabase.favoriteDao().insertOrIgnoreFromApi(list)
    }

    suspend fun deleteMovieList() {
        movieDatabase.movieDao().deleteAll()
    }

    suspend fun toggleFavoriteMovie(movie: MovieEntity) {
        return movieDatabase.favoriteDao().insertOrUpdate(
            FavoriteDBModel(movie.apiId, movie.isFavorite)
        )
    }
}