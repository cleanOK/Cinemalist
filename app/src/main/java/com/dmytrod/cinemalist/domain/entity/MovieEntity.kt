package com.dmytrod.cinemalist.domain.entity

import com.dmytrod.cinemalist.data.db.model.MovieDBModel
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel

data class MovieEntity(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String
) {
    companion object Mapper {
        val fromRemote = fun(it: MovieAPIModel): MovieEntity {
            return MovieEntity(it.id, it.title, it.overview, it.posterPath)
        }

        val fromDB = fun(it: MovieDBModel): MovieEntity {
            return MovieEntity(it.uid, it.title, it.overview, it.posterPath)
        }
    }
}