package com.dmytrod.cinemalist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.DataSourceFactoryInteractor
import com.dmytrod.cinemalist.domain.interactor.FlowInteractor
import com.dmytrod.cinemalist.domain.interactor.PageData
import com.dmytrod.cinemalist.presentation.MovieBoundaryCallback
import com.dmytrod.cinemalist.presentation.MovieListState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OngoingMoviesViewModel(
    getMovies: DataSourceFactoryInteractor<MovieEntity>,
    fetchMoviesByPage: FlowInteractor<Int, PageData>,
    toggleFavoriteMovie: FlowInteractor<MovieEntity, Unit>,
    private val removeMoviesFromDB: FlowInteractor<Unit, Unit>
) : BaseMoviesViewModel(getMovies, toggleFavoriteMovie) {

    private val boundaryCallback =
        MovieBoundaryCallback(fetchMoviesByPage, viewModelScope, movieListState)

    override fun getPagedListDataBuilder(): LivePagedListBuilder<Int, MovieEntity> {
        Log.d("TEST", "boundary callback set")
        return super.getPagedListDataBuilder()
            .setBoundaryCallback(boundaryCallback)
    }

    @InternalCoroutinesApi
    override fun refreshMovieList() {
        viewModelScope.launch {
            try {
                removeMoviesFromDB.execute(Unit).collect {
                    //TODO handle Result
                    pagedListLiveData.value?.dataSource?.invalidate()
                    boundaryCallback.reset()
                }
            } catch (e: Throwable) {
                Log.e("TEST", "refreshing failed", e)
                movieListState.value =
                    MovieListState.Error(
                        errorMessageRes = R.string.failed_refresh
                    )
            }
        }

    }
}
