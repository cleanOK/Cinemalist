package com.dmytrod.cinemalist.domain.entity

import com.dmytrod.cinemalist.data.remote.model.MovieModel

data class MovieEntity(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String
) {
    companion object Mapper {
        val fromRemote = fun(it: MovieModel): MovieEntity {
            return MovieEntity(it.id, it.title, it.overview, it.posterPath)
        }
    }
}