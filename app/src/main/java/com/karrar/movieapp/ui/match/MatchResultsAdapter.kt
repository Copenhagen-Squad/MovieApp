package com.karrar.movieapp.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.databinding.ItemMatchResultBinding

class MatchResultsAdapter(
    private val callbacks: MatchResultCallbacks
) : ListAdapter<MatchItemUI, MatchResultsAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<MatchItemUI>() {
        override fun areItemsTheSame(a: MatchItemUI, b: MatchItemUI) = a.id == b.id
        override fun areContentsTheSame(a: MatchItemUI, b: MatchItemUI) = a == b
    }

    inner class VH(val binding: ItemMatchResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMatchResultBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.callbacks = callbacks
        holder.binding.executePendingBindings()
    }
}
