package com.dmytrod.cinemalist.ui.home

import android.content.Intent
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
import com.dmytrod.cinemalist.presentation.MovieListState
import com.dmytrod.cinemalist.presentation.viewmodel.BaseMoviesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

abstract class BaseMoviesFragment : Fragment() {
    protected abstract val moviesViewModel: BaseMoviesViewModel
    private val onFavoriteClick: (item: MovieEntity) -> Unit = {
        moviesViewModel.toggleFavorite(it)
    }
    private val onShareClick: (item: MovieEntity) -> Unit = {
        shareMovie(it)
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
            binding.movieRefresh.isRefreshing = it is MovieListState.Loading
            if (it is MovieListState.Error) handleError(it)
        })
        moviesViewModel.pagedListLiveData.observe(viewLifecycleOwner,
            Observer { movieAdapter.submitList(it) })
        binding.movieRefresh.isEnabled = isRefreshEnabled()
        binding.movieRefresh.setOnRefreshListener { moviesViewModel.refreshMovieList() }
    }

    protected abstract fun isRefreshEnabled(): Boolean

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupMovieRecycler() {
        val movieRecycler = binding.movieRecycler
        movieRecycler.adapter = movieAdapter
        movieRecycler.addItemDecoration(ItemOffsetDecoration(resources.displayMetrics))
        movieRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun handleError(error: MovieListState.Error) {
        if (!error.isHandled) {
            Snackbar.make(requireView(), error.errorMessageRes, Snackbar.LENGTH_SHORT)
                .show()
            error.isHandled = true
        }
    }

    private fun shareMovie(item: MovieEntity) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, item.title)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}