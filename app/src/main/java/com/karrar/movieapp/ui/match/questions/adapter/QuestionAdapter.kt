package com.karrar.movieapp.ui.match.questions.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.karrar.movieapp.BR
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.match.questions.Choice
import com.karrar.movieapp.ui.match.questions.Question
import com.karrar.movieapp.ui.match.questions.QuestionInteractionListener
import com.karrar.movieapp.ui.match.questions.QuestionType

class QuestionAdapter(
    private var items: MutableList<Question>,
    private val listener: QuestionInteractionListener,
    private val onSelect: (List<Choice>, QuestionType) -> Unit,
) : BaseAdapter<Question>(items, listener) {

    override val layoutID: Int = R.layout.list_question_choices
    private val choicesAdapters = mutableMapOf<Int, ChoicesAdapter>()

    override fun bind(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        val choicesAdapter = choicesAdapters.getOrPut(position) {
            ChoicesAdapter(item.choices, item.type, listener, onSelect)
        }

        val layoutManager = getLayoutManager(holder.itemView.context, item)

        holder.binding.apply {
            setVariable(BR.question, item.question)
            setVariable(BR.layoutManager, layoutManager)
            setVariable(BR.adapterRecycler, choicesAdapter)
            setVariable(BR.isCurrent, !item.isAnswered)
            executePendingBindings()
        }
    }

    fun emitItems(items: List<Question>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private fun getLayoutManager(context: Context, item: Question): RecyclerView.LayoutManager {
        return when (item.type) {
            QuestionType.MOOD -> GridLayoutManager(context, 2)
            QuestionType.GENRE -> FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            QuestionType.MEDIA_RUNTIME -> LinearLayoutManager(context)
            QuestionType.TIME_PERIOD -> {
                if (item.choices.size <= 3) GridLayoutManager(context, item.choices.size)
                else GridLayoutManager(context, 1)
            }
        }
    }
}
