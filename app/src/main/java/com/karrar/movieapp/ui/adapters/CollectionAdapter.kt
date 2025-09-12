package com.karrar.movieapp.ui.adapters

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.models.CollectionUiState

class CollectionAdapter(
    items: List<CollectionUiState>,
    val listener: CollectionInteractionListener
) :
    BaseAdapter<CollectionUiState>(items, listener) {
    override val layoutID: Int = R.layout.collection_item
}

interface CollectionInteractionListener : BaseInteractionListener {
    fun onCollectionClick(collectionId: Int)
    fun onClickSeeAllCollection()
}