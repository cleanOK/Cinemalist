package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.db.MovieDatabase
import com.dmytrod.cinemalist.data.db.model.FavoriteDBModel
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel
import com.dmytrod.cinemalist.domain.entity.MovieEntity

class PersistenceRepository(private val movieDatabase: MovieDatabase) : IPersistenceRepository {
    override fun getMovies() = movieDatabase.movieDao().getFavoriteMovies()

    override suspend fun storeList(list: List<MovieAPIModel>) {
        movieDatabase.movieDao().insertOrUpdateFromApi(list)
        movieDatabase.favoriteDao().insertOrIgnoreFromApi(list)
    }

    override suspend fun deleteMovieList() {
        movieDatabase.movieDao().deleteAll()
    }

    override suspend fun toggleFavoriteMovie(movie: MovieEntity) =
        movieDatabase.favoriteDao()
            .insertOrUpdate(FavoriteDBModel(movie.apiId, !movie.isFavorite))
}