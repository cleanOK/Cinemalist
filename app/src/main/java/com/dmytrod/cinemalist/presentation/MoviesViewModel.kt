package com.dmytrod.cinemalist.presentation

import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dmytrod.cinemalist.domain.OngoingMoviesState
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.FetchMoviesByPage
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

class MoviesViewModel(
    getOngoingMovies: GetOngoingMovies,
    private val fetchMoviesByPage: FetchMoviesByPage
) : ViewModel() {
    val movieList: LiveData<PagedList<MovieEntity>>

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(3)
            .setPageSize(GetOngoingMovies.PAGE_SIZE)
            .build()

        val pagedList = LivePagedListBuilder(getOngoingMovies.executeFromDB(), pagedListConfig)
            .setBoundaryCallback(MovieBoundaryCallback(fetchMoviesByPage, viewModelScope))
            .build()
        movieList = pagedList
    }

    //    val movieList = getOngoingMovies.executeFromDB()}
    private class MovieBoundaryCallback(
        private val fetchMoviesByPage: FetchMoviesByPage,
        private val viewModelScope: CoroutineScope
    ) :
        PagedList.BoundaryCallback<MovieEntity>() {
        private val requestArray = SparseArray<Job>()
        private var nextPage = 1
        private var lastPage = 1
        private val collector = object : FlowCollector<OngoingMoviesState> {
            override suspend fun emit(value: OngoingMoviesState) {
                when (value) {
                    is OngoingMoviesState.Success -> {
                        lastPage = calculateLastPage(value.totalPages)
                        nextPage++
                    }
                    is OngoingMoviesState.Error -> {

                    }
                    is OngoingMoviesState.Loading -> {

                    }
                    OngoingMoviesState.Empty -> {

                    }
                }
            }
        }

        @InternalCoroutinesApi
        override fun onZeroItemsLoaded() {
            if (requestArray.get(1) == null) {
                requestArray.put(
                    1, viewModelScope.launch {
                        fetchMoviesByPage.execute(1)
                            .collect(collector)
                    })
            }
        }


        @InternalCoroutinesApi
        override fun onItemAtEndLoaded(itemAtEnd: MovieEntity) {
            if (nextPage > lastPage) {
                return
            }
            if (requestArray.get(nextPage) == null) {
                requestArray.put(
                    nextPage, viewModelScope.launch {
                        fetchMoviesByPage.execute(nextPage)
                            .collect(collector)
                    })
            }
        }

        override fun onItemAtFrontLoaded(itemAtFront: MovieEntity) {
            // ignored, since we only ever append to what's in the DB
        }

        private fun calculateLastPage(totalPages: Int): Int =
            totalPages / GetOngoingMovies.PAGE_SIZE + if (totalPages % GetOngoingMovies.PAGE_SIZE == 0) 0 else 1
    }

}