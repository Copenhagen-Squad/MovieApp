package com.karrar.movieapp.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.databinding.ItemMatchPosterBinding
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.MovieDetailsUIState

class MatchPostersAdapter
    : ListAdapter<MovieDetailsUIState, MatchPostersAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<MovieDetailsUIState>() {
        override fun areItemsTheSame(a: MovieDetailsUIState, b: MovieDetailsUIState) = a.id == b.id
        override fun areContentsTheSame(a: MovieDetailsUIState, b: MovieDetailsUIState) = a == b
    }

    inner class VH(val binding: ItemMatchPosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return VH(ItemMatchPosterBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.item = getItem(position)
        holder.binding.executePendingBindings()
    }
}
