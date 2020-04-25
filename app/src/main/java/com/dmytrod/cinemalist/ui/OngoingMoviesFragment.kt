package com.dmytrod.cinemalist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.databinding.FragmentMovieListBinding
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.presentation.MovieViewModel
import com.dmytrod.cinemalist.presentation.OngoingMoviesState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OngoingMoviesFragment : Fragment() {
    private val moviesViewModel by sharedViewModel<MovieViewModel>()
    private val onFavoriteClick: (item: MovieEntity) -> Unit = {
        moviesViewModel.toggleFavorite(it)
    }
    private val onShareClick: (item: MovieEntity) -> Unit = {
        //TODO implement
    }
    private val movieAdapter = MovieAdapter(onFavoriteClick, onShareClick)
    private var _binding: FragmentMovieListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = moviesViewModel
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMovieRecycler()
        moviesViewModel.getMovieListState().observe(viewLifecycleOwner, Observer {
            binding.movieRefresh.isRefreshing = it is OngoingMoviesState.Loading
            if (it is OngoingMoviesState.Error) handleError(it)
        })
        moviesViewModel.getPagedListLiveData().observe(viewLifecycleOwner,
            Observer { movieAdapter.submitList(it) })
        binding.movieRefresh.setOnRefreshListener { moviesViewModel.refreshMovieList() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupMovieRecycler() {
        val movieRecycler = binding.movieRecycler
        movieRecycler.adapter = movieAdapter
        movieRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun handleError(error: OngoingMoviesState.Error) {
        if (!error.isHandled) {
            Snackbar.make(requireView(), error.errorMessageRes, Snackbar.LENGTH_SHORT)
                .show()
            error.isHandled = true
        }
    }
}
