package com.dmytrod.cinemalist.presentation.viewmodel

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
import timber.log.Timber

class OngoingMoviesViewModel(
    getMovies: DataSourceFactoryInteractor<MovieEntity>,
    fetchMoviesByPage: FlowInteractor<Int, PageData>,
    toggleFavoriteMovie: FlowInteractor<MovieEntity, Unit>,
    private val removeMoviesFromDB: FlowInteractor<Unit, Unit>
) : BaseMoviesViewModel(getMovies, toggleFavoriteMovie) {

    private val boundaryCallback =
        MovieBoundaryCallback(fetchMoviesByPage, viewModelScope, movieListState)

    override fun getPagedListDataBuilder(): LivePagedListBuilder<Int, MovieEntity> {
        Timber.d("boundary callback set")
        return super.getPagedListDataBuilder()
            .setBoundaryCallback(boundaryCallback)
    }

    @InternalCoroutinesApi
    override fun refreshMovieList() {
        viewModelScope.launch {
            try {
                removeMoviesFromDB.execute(Unit).collect {
                    pagedListLiveData.value?.dataSource?.invalidate()
                    boundaryCallback.reset()
                }
            } catch (e: Throwable) {
                Timber.e(e, "refreshing failed")
                movieListState.value =
                    MovieListState.Error(
                        errorMessageRes = R.string.failed_refresh
                    )
            }
        }

    }
}
