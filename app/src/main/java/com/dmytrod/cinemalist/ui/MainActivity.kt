package com.dmytrod.cinemalist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.presentation.MoviesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val moviesViewModel by viewModel<MoviesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        moviesViewModel.getMovies()
    }
}
