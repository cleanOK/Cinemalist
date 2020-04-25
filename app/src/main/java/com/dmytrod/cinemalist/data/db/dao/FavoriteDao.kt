package com.dmytrod.cinemalist.data.db.dao

import androidx.room.Dao
import androidx.room.Transaction
import com.dmytrod.cinemalist.data.db.model.FavoriteDBModel
import com.dmytrod.cinemalist.data.remote.model.MovieAPIModel

@Dao
abstract class FavoriteDao : BaseDao<FavoriteDBModel>() {

    @Transaction
    open suspend fun insertOrIgnoreFromApi(movies: List<MovieAPIModel>) {
        insert(movies.map(FavoriteDBModel.fromRemote))
    }
}