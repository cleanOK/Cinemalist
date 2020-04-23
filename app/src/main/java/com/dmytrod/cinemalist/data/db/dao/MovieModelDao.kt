package com.dmytrod.cinemalist.data.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.dmytrod.cinemalist.data.db.model.MovieDBModel

@Dao
interface MovieModelDao {
    @Query("SELECT * FROM ${MovieDBModel.TABLE_NAME} WHERE apiId = :id")
    suspend fun getMovieByApiId(id: Int): MovieDBModel?

    @Query("SELECT * FROM ${MovieDBModel.TABLE_NAME}")
    fun getMovies(): DataSource.Factory<Int, MovieDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieDBModel)

    @Transaction
    suspend fun insertOrUpdate(movies: List<MovieDBModel>) {
        movies.forEach {
            //Check for existing entities to avoid duplicates
            val alreadyExistingModel = getMovieByApiId(it.apiId)
            if (alreadyExistingModel != null) {
                it.uid = alreadyExistingModel.uid
            }
            insert(it)
        }
    }

    @Delete
    suspend fun delete(movie: MovieDBModel)

    @Query("DELETE FROM ${MovieDBModel.TABLE_NAME}")
    suspend fun deleteAll()
}
