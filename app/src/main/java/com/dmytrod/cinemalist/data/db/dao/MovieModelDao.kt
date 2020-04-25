package com.dmytrod.cinemalist.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dmytrod.cinemalist.data.db.model.FavorableMovieModel
import com.dmytrod.cinemalist.data.db.model.MovieDBModel
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel

@Dao
abstract class MovieModelDao : BaseDao<MovieDBModel>() {

    @Query("SELECT * FROM ${MovieDBModel.TABLE_NAME} WHERE api_id = :id")
    abstract suspend fun getMovieByApiId(id: Int): MovieDBModel?

    @Query(
        "SELECT movie.api_id, movie.overview, movie.poster_path, movie.title, favorite.is_favorite FROM movie, favorite WHERE movie.api_id == favorite.movie_api_id"
    )
    abstract fun getAllMovies(): DataSource.Factory<Int, FavorableMovieModel>

    @Query(
        "SELECT movie.api_id, movie.overview, movie.poster_path, movie.title, favorite.is_favorite FROM movie, favorite WHERE movie.api_id == favorite.movie_api_id AND favorite.is_favorite = 1"
    )
    abstract fun getFavoriteMovies(): DataSource.Factory<Int, FavorableMovieModel>

    @Transaction
    open suspend fun insertOrUpdateFromApi(movies: List<MovieAPIModel>) {
        movies.forEach {
            //Check for existing entities to avoid duplicates
            val alreadyExistingModel = getMovieByApiId(it.id)
            val movieDBModel = MovieDBModel.fromRemote(it)
            if (alreadyExistingModel != null) {
                movieDBModel.uid = alreadyExistingModel.uid
            }
            insertOrUpdate(movieDBModel)
        }
    }

    @Query("DELETE FROM ${MovieDBModel.TABLE_NAME}")
    abstract suspend fun deleteAll()

}
