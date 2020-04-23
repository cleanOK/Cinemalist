package com.dmytrod.cinemalist.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class FavorableMovieModel(
    @Embedded val movieDBModel: MovieDBModel,
    @Relation(
        parentColumn = "api_id",
        entityColumn = "movie_api_id"
    )
    val favoriteDBModel: FavoriteDBModel
)