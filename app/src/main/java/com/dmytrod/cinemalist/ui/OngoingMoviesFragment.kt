package com.dmytrod.cinemalist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmytrod.cinemalist.R
import com.dmytrod.cinemalist.databinding.FragmentMovieListBinding
import com.dmytrod.cinemalist.databinding.ItemMovieBinding
import com.dmytrod.cinemalist.domain.OngoingMoviesState
import com.dmytrod.cinemalist.domain.entity.MovieEntity
import com.dmytrod.cinemalist.presentation.MoviesViewModel
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
        moviesViewModel.movieList.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
//            when (it) {
//                is OngoingMoviesState.Success -> movieAdapter.submitList(it.data)
//                is OngoingMoviesState.Loading -> {
//                    //TODO show progress
//                }
//                is OngoingMoviesState.Empty -> {
//                    //TODO shoe empty state
//                }
////                TODO show offline badge
//                is OngoingMoviesState.Error ->
//                    Snackbar.make(view, it.remoteError.errorMessageRes, Snackbar.LENGTH_SHORT)
//                        .show()
//            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    class MovieAdapter : PagedListAdapter<MovieEntity, MovieAdapter.ViewHolder>(diffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        class ViewHolder(private val binding: ItemMovieBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item: MovieEntity?) {
                binding.movie = item
                binding.executePendingBindings()
            }

        }

        companion object {
            private val diffCallback = object : DiffUtil.ItemCallback<MovieEntity>() {
                override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
                    oldItem == newItem
            }
        }
    }
}
