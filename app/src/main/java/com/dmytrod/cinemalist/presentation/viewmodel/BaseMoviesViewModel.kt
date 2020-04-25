package com.dmytrod.cinemalist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.DataSourceFactoryInteractor
import com.dmytrod.cinemalist.domain.interactor.FlowInteractor
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies
import com.dmytrod.cinemalist.domain.interactor.Result
import com.dmytrod.cinemalist.presentation.MovieListState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseMoviesViewModel(
    getMovies: DataSourceFactoryInteractor<MovieEntity>,
    private val toggleFavoriteMovie: FlowInteractor<MovieEntity, Unit>
) : ViewModel() {
    protected val movieListState = MutableLiveData<MovieListState>()
    private val pagedListConfig: PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPrefetchDistance(15)
        .setPageSize(GetOngoingMovies.PAGE_SIZE)
        .build()
    private val pagedListDataBuilder = LivePagedListBuilder(getMovies.execute(), pagedListConfig)
    val pagedListLiveData by lazy { getPagedListDataBuilder().build() }

    //state helpers for Data Binding
    val isListLoading = movieListState.map { it is MovieListState.Loading }
    val isListEmpty = movieListState.map { it is MovieListState.Empty }

    abstract fun refreshMovieList()

    protected open fun getPagedListDataBuilder() = pagedListDataBuilder

    fun getMovieListState(): LiveData<MovieListState> = movieListState

    fun toggleFavorite(movieEntity: MovieEntity) {
        viewModelScope.launch {
            toggleFavoriteMovie.execute(movieEntity).collect {
                when (it) {
                    is Result.Success<Unit> ->
                        Log.d(
                            "TEST",
                            "movie ${movieEntity.title} is now ${if (movieEntity.isFavorite) "unfaved" else "faved"}"
                        )
                    is Result.Failure<Unit> -> {
                        //TODO handle error
                    }
                }
            }
        }
    }

}