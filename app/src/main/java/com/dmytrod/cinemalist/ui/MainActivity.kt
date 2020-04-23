package com.dmytrod.cinemalist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.presentation.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val moviesViewModel by viewModel<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
