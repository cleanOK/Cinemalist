package com.dmytrod.cinemalist.ui.home

import com.dmytrod.cinemalist.presentation.ONGOING
import com.dmytrod.cinemalist.presentation.viewmodel.BaseMoviesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named

class OngoingMoviesFragment : BaseMoviesFragment() {
    override val moviesViewModel: BaseMoviesViewModel by sharedViewModel(named(ONGOING))

    override fun isRefreshEnabled(): Boolean = true

    companion object {
        fun newInstance() = OngoingMoviesFragment()
    }
}
