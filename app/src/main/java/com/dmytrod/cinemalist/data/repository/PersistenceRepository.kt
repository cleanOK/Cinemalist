package com.dmytrod.cinemalist.data.repository

import com.dmytrod.cinemalist.data.db.MovieDatabase
import com.dmytrod.cinemalist.data.db.model.MovieDBModel

class PersistenceRepository(private val movieDatabase: MovieDatabase) {
    fun getMovies() = movieDatabase.movieDao().getMovies()
    suspend fun storeList(list: List<MovieDBModel>) {
        movieDatabase.movieDao().insert(list)
    }
}