package com.dmytrod.cinemalist.data.db.model

import androidx.room.ColumnInfo

data class FavorableMovieModel(
    @ColumnInfo(name = "api_id")
    val apiId: Int,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
)