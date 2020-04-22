package com.dmytrod.cinemalist.data.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.dmytrod.cinemalist.data.db.model.MovieDBModel

@Dao
interface MovieModelDao {
    @Query("SELECT * FROM ${MovieDBModel.TABLE_NAME} WHERE uid = :id")
    suspend fun getMovie(id: Int): MovieDBModel

    @Query("SELECT * FROM ${MovieDBModel.TABLE_NAME}")
    fun getMovies(): DataSource.Factory<Int, MovieDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieDBModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: List<MovieDBModel>)

    @Delete
    suspend fun delete(movie: MovieDBModel)
}
