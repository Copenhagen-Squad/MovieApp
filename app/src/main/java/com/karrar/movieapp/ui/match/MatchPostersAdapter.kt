package com.karrar.movieapp.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.databinding.ItemMatchPosterBinding

class MatchPostersAdapter
    : ListAdapter<MatchItemUI, MatchPostersAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<MatchItemUI>() {
        override fun areItemsTheSame(a: MatchItemUI, b: MatchItemUI) = a.id == b.id
        override fun areContentsTheSame(a: MatchItemUI, b: MatchItemUI) = a == b
    }

    inner class VH(val binding: ItemMatchPosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemMatchPosterBinding.inflate(inf, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.item = getItem(position)
        holder.binding.executePendingBindings()
    }
}