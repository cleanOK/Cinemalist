package com.dmytrod.cinemalist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dmytrod.cinemalist.domain.interactor.GetOngoingMovies

class MoviesViewModel(getOngoingMovies: GetOngoingMovies) : ViewModel() {
    val movieList = getOngoingMovies.execute().asLiveData()
}