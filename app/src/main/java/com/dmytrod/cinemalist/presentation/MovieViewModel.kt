package com.dmytrod.cinemalist.presentation

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieViewModel(
    getOngoingMovies: DataSourceFactoryInteractor<MovieEntity>,
    fetchMoviesByPage: FlowInteractor<Int, PageData>,
    private val toggleFavoriteMovie: FlowInteractor<MovieEntity, Unit>,
    private val removeMoviesFromDB: FlowInteractor<Unit, Unit>
) : ViewModel() {
    private val movieListState = MutableLiveData<OngoingMoviesState>()

    //TODO move to DI
    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPrefetchDistance(15)
        .setPageSize(GetOngoingMovies.PAGE_SIZE)
        .build()
    private val boundaryCallback =
        MovieBoundaryCallback(fetchMoviesByPage, viewModelScope, movieListState)
    private val pagedListLiveData =
        LivePagedListBuilder(getOngoingMovies.execute(), pagedListConfig)
            .setBoundaryCallback(boundaryCallback)
            .build()

    //state helpers for Data Binding
    val isListLoading = movieListState.map { it is OngoingMoviesState.Loading }
    val isListEmpty = movieListState.map { it is OngoingMoviesState.Empty }

    fun getMovieListState(): LiveData<OngoingMoviesState> = movieListState

    fun getPagedListLiveData(): LiveData<PagedList<MovieEntity>> = pagedListLiveData

    @InternalCoroutinesApi
    fun refreshMovieList() {
        viewModelScope.launch {
            try {
                removeMoviesFromDB.execute(Unit).collect {
                    pagedListLiveData.value?.dataSource?.invalidate()
                    boundaryCallback.reset()
                }
            } catch (e: Throwable) {
                Log.e("TEST", "refreshing failed", e)
                movieListState.value =
                    OngoingMoviesState.Error(errorMessageRes = R.string.failed_refresh)
            }
        }
    }

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