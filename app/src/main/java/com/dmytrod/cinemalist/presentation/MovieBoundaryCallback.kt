package com.dmytrod.cinemalist.presentation

import android.util.SparseArray
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.FlowInteractor
import com.dmytrod.cinemalist.domain.interactor.PageData
import com.dmytrod.cinemalist.domain.interactor.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieBoundaryCallback(
    private val fetchMoviesByPage: FlowInteractor<Int, PageData>,
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
            //here loading of next page if necessary
            requestArray.put(nextPage, viewModelScope.launch {
                fetchMoviesByPage.execute(nextPage)
                    .collect { handlePageResult(it) }
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

    private fun handleFirstPageResult(result: Result<PageData>) {
        handlePageResult(result)
        if (result is Result.Success<PageData>) {
            movieListState.value =
                if (lastPage > 0) OngoingMoviesState.Success else OngoingMoviesState.Empty
        }
    }

    private fun handlePageResult(result: Result<PageData>) {
        when (result) {
            is Result.Success<PageData> -> {
                lastPage = result.data.totalPages
                nextPage = result.data.page + 1
            }
            is Result.Failure<PageData> -> {
                movieListState.value = OngoingMoviesState.Error(result.errorMessageRes)
            }
        }
    }

}