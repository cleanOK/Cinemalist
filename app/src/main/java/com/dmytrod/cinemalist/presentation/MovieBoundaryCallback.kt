package com.dmytrod.cinemalist.presentation

import android.util.Log
import android.util.SparseArray
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.FetchMoviesByPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieBoundaryCallback(
    private val fetchMoviesByPage: FetchMoviesByPage,
    private val viewModelScope: CoroutineScope,
    private val movieListState: MutableLiveData<OngoingMoviesState>
) :
    PagedList.BoundaryCallback<MovieEntity>() {
    private val requestArray = SparseArray<Job>()
    private var nextPage = 1
    private var lastPage = 1

    @InternalCoroutinesApi
    override fun onZeroItemsLoaded() {
        if (requestArray.get(1) == null) {
            movieListState.value = OngoingMoviesState.Loading
            requestArray.put(1, viewModelScope.launch {
                fetchMoviesByPage.execute(1)
                    .collect { handleFirstPageResult(it) }
            })
        }
    }

    @InternalCoroutinesApi
    override fun onItemAtEndLoaded(itemAtEnd: MovieEntity) {
        if (nextPage > lastPage) return
        if (requestArray.get(nextPage) == null) {
            requestArray.put(nextPage, viewModelScope.launch {
                fetchMoviesByPage.execute(nextPage)
                    .collect { handleNextPageResult(it) }
            })
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: MovieEntity) {
        // ignored, since we only ever append to what's in the DB
    }

    fun reset() {
        requestArray.clear()
        lastPage = 1
        nextPage = 1
    }

    //TODO extract handle*Result functions to ViewModel
    private fun handleFirstPageResult(result: FetchMoviesByPage.Result) {
        when (result) {
            is FetchMoviesByPage.Result.Success -> {
                lastPage = result.totalPages
                nextPage = result.page + 1
                movieListState.value =
                    if (lastPage > 0) OngoingMoviesState.Success else OngoingMoviesState.Empty
            }
            is FetchMoviesByPage.Result.Failure -> {
                movieListState.value = OngoingMoviesState.Error(result.errorMessageRes)
            }
        }
    }

    //TODO extract handle*Result functions to ViewModel
    private fun handleNextPageResult(result: FetchMoviesByPage.Result) {
        when (result) {
            is FetchMoviesByPage.Result.Success -> {
                lastPage = result.totalPages
                nextPage = result.page + 1
            }
            is FetchMoviesByPage.Result.Failure -> {
                //TODO
            }
        }
    }

}