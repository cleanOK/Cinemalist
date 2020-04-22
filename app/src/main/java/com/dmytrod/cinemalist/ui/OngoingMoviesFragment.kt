package com.dmytrod.cinemalist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.databinding.FragmentMovieListBinding
import com.dmytrod.cinemalist.presentation.MoviesViewModel
import com.dmytrod.cinemalist.presentation.OngoingMoviesState
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OngoingMoviesFragment : Fragment() {
    private val moviesViewModel by sharedViewModel<MoviesViewModel>()
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieRecycler = binding.movieRecycler
        val movieAdapter = MovieAdapter()
        movieRecycler.adapter = movieAdapter
        movieRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        moviesViewModel.getMovieListState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is OngoingMoviesState.Success -> {
                    //TODO?
                }
                is OngoingMoviesState.Loading -> {
                    //TODO show progress
                }
                is OngoingMoviesState.Empty -> {
                    //TODO shoe empty state
                }
//                TODO show offline badge
                is OngoingMoviesState.Error -> {
                    Snackbar.make(view, it.errorMessageRes, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })
        moviesViewModel.getPagedListLiveData().observe(viewLifecycleOwner, Observer {
                binding.movieRefresh.isRefreshing = false
                Log.d("TEST", "submit new list")
                movieAdapter.submitList(it)
        })
        binding.movieRefresh.setOnRefreshListener {
            moviesViewModel.refreshMovieList()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
