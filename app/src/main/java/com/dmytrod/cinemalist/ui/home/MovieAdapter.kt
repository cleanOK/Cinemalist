package com.dmytrod.cinemalist.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmytrod.cinemalist.databinding.ItemMovieBinding
import com.dmytrod.cinemalist.domain.entity.MovieEntity

class MovieAdapter(
    private val onFavoriteClick: (item: MovieEntity) -> Unit,
    private val onShareClick: (item: MovieEntity) -> Unit
) : PagedListAdapter<MovieEntity, MovieAdapter.ViewHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onFavoriteClick,
            onShareClick
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemMovieBinding,
        private val onFavoriteClick: (item: MovieEntity) -> Unit,
        private val onShareClick: (item: MovieEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieEntity?) {
            if (item == null) {
                //case when PagedList.Config has setEnablePlaceholders(true)
            } else {
                binding.movie = item
                binding.favoriteView.setOnClickListener {
                    onFavoriteClick.invoke(item)
                }
                binding.shareView.setOnClickListener {
                    onShareClick.invoke(item)
                }
                binding.executePendingBindings()
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
                oldItem.apiId == newItem.apiId

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity) =
                oldItem == newItem
        }
    }
}