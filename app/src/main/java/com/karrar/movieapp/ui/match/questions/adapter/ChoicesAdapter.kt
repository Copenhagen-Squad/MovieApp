package com.karrar.movieapp.ui.match.questions.adapter

import com.karrar.movieapp.BR
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.match.questions.Choice
import com.karrar.movieapp.ui.match.questions.QuestionInteractionListener
import com.karrar.movieapp.ui.match.questions.QuestionType

class ChoicesAdapter(
    private val items: List<Choice>,
    private val type: QuestionType,
    listener: QuestionInteractionListener,
    private val onItemSelected: (List<Choice>, QuestionType) -> Unit,
) : BaseAdapter<Choice>(items, listener) {
    override val layoutID: Int
        get() =
            when (type) {
                QuestionType.GENRE -> R.layout.item_genre_question_choice
                else -> R.layout.item_question_choice
            }
    private var selectedItems = mutableListOf<Choice>()

    override fun bind(
        holder: ItemViewHolder,
        position: Int,
    ) {
        val item = items[position]
        val mListener =
            object : ChoicesInteractionListener {
                override fun onChoiceSelected(choice: Choice) {
                    when (type) {
                        QuestionType.MEDIA_RUNTIME, QuestionType.TIME_PERIOD -> {
                            if (selectedItems.isNotEmpty()) {
                                val previousSelectedIndex = selectedItems.first()
                                selectedItems.clear()
                                notifyItemChanged(items.indexOf(previousSelectedIndex))
                            }
                            selectedItems.add(choice)
                            notifyItemChanged(items.indexOf(choice))
                        }
                        else -> {
                            if (selectedItems.contains(choice)) {
                                selectedItems.remove(choice)
                            } else {
                                selectedItems.add(choice)
                            }
                            notifyItemChanged(items.indexOf(choice))
                        }
                    }
                    onItemSelected(selectedItems, type)
                }
            }

        holder.binding.apply {
            setVariable(BR.choice, item)
            setVariable(BR.listener, mListener)
            setVariable(BR.isSelected, selectedItems.contains(item))
            executePendingBindings()
        }
    }

    interface ChoicesInteractionListener : BaseInteractionListener {
        fun onChoiceSelected(choice: Choice)
    }
}