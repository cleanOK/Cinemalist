package com.dmytrod.cinemalist.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmytrod.cinemalist.data.db.model.MovieDBModel.Companion.TABLE_NAME
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel

@Entity(tableName = TABLE_NAME)
data class MovieDBModel(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "posterPath")
    val posterPath: String

) {
    companion object {
        val fromRemote = fun(it: MovieAPIModel): MovieDBModel {
            return MovieDBModel(it.id, it.title, it.overview, it.posterPath)
        }
        const val TABLE_NAME = "movie"
    }
}