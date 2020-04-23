package com.dmytrod.cinemalist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.FetchMoviesByPage
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies
import com.dmytrod.cinemalist.domain.interactor.RemoveMoviesFromDB
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoviesViewModel(
    //TODO depend on abstraction
    getOngoingMovies: GetOngoingMovies,
    fetchMoviesByPage: FetchMoviesByPage,
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


    @InternalCoroutinesApi
    fun refreshMovieList() {
        viewModelScope.launch {
            try {
                removeMoviesFromDB.execute().collect {
                    boundaryCallback.reset()
                }
            } catch (e: Throwable) {
                //TODO
                Log.e("TEST", "refreshing failed", e)
            }
        }
    }

    fun getMovieListState(): LiveData<OngoingMoviesState> = movieListState

    fun getPagedListLiveData(): LiveData<PagedList<MovieEntity>> = pagedListLiveData

}