package com.dmytrod.cinemalist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dmytrod.cinemalist.data.db.dao.MovieModelDao
import com.dmytrod.cinemalist.data.db.model.MovieDBModel

@Database(entities = [MovieDBModel::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieModelDao
    companion object {
        const val DB_FILE_NAME = "movie_db"
    }
}