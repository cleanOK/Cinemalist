package com.dmytrod.cinemalist.presentation

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.FetchMoviesByPage
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies
import com.dmytrod.cinemalist.domain.interactor.RemoveMoviesFromDB
import com.dmytrod.cinemalist.domain.interactor.ToggleFavoriteMovie
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieViewModel(
    //TODO depend on abstraction
    getOngoingMovies: GetOngoingMovies,
    fetchMoviesByPage: FetchMoviesByPage,
    private val toggleFavoriteMovie: ToggleFavoriteMovie,
    private val removeMoviesFromDB: RemoveMoviesFromDB
) : ViewModel() {
    private val movieListState = MutableLiveData<OngoingMoviesState>()
    private val dataSourceFactory = getOngoingMovies.execute()
    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPrefetchDistance(15)
        .setPageSize(GetOngoingMovies.PAGE_SIZE)
        .build()
    private val boundaryCallback =
        MovieBoundaryCallback(fetchMoviesByPage, viewModelScope, movieListState)
    private val pagedListLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
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
                removeMoviesFromDB.execute().collect {
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
            try {
                toggleFavoriteMovie.execute(movieEntity).collect {
                    when (it) {
                        is ToggleFavoriteMovie.Result.Success ->
                            Log.d(
                                "TEST",
                                "movie ${movieEntity.title} is now ${if (movieEntity.isFavorite) "unfaved" else "faved"}"
                            )
                        is ToggleFavoriteMovie.Result.Failure -> {
                            //TODO
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.e("TEST", "failed to toggle favorite movie ${movieEntity.title}", e)
            }
        }
    }

}