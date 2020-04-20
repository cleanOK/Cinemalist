package com.dmytrod.cinemalist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies
import kotlinx.coroutines.launch

class MoviesViewModel(private val getOngoingMovies: GetOngoingMovies) : ViewModel() {
    private val movieList = MutableLiveData<List<MovieEntity>>()

    fun getMovies() {
        viewModelScope.launch {
            movieList.postValue(getOngoingMovies.execute())
        }
    }
}