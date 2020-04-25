package com.dmytrod.cinemalist.ui.home

import com.dmytrod.cinemalist.presentation.FAVORITE
import com.dmytrod.cinemalist.presentation.viewmodel.BaseMoviesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named

class FavoriteMoviesFragment : BaseMoviesFragment() {
    override val moviesViewModel: BaseMoviesViewModel by sharedViewModel(named(FAVORITE))

    override fun isRefreshEnabled(): Boolean = false

    companion object {
        fun newInstance() = FavoriteMoviesFragment()
    }
}