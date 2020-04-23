package com.dmytrod.cinemalist.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel

@Entity(tableName = FavoriteDBModel.TABLE_NAME)
data class FavoriteDBModel(
    @PrimaryKey
    @ColumnInfo(name = "movie_api_id")
    val apiId: Int,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
) {
    companion object {
        const val TABLE_NAME = "favorite"
        val fromRemote = fun(it: MovieAPIModel): FavoriteDBModel =
            FavoriteDBModel(apiId = it.id, isFavorite = false)

    }

}