package com.karrar.movieapp.ui.match.questions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.data.MatchMapper
import com.karrar.movieapp.databinding.FragmentMatchQuestionsBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.match.questions.adapter.QuestionAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MatchQuestionsFragment : BaseFragment<FragmentMatchQuestionsBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_match_questions
    override val viewModel: MatchQuestionsViewModel by viewModels()
    var currentQuestionType = QuestionType.MOOD
    private lateinit var questionAdapter: QuestionAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
        viewModel.getData()
        questionAdapter =
            QuestionAdapter(
                mutableListOf(),
                viewModel,
            ) { selectedChoices, type ->
                viewModel.onChoiceSelected(type, selectedChoices)
            }
        binding.vm = viewModel
        binding.matchQuestionsRv.adapter = questionAdapter

        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                render(state)
            }
        }

        lifecycleScope.launch {
            viewModel.questions.collectLatest { state ->
                questionAdapter.emitItems(state)
            }
        }

        // Navigate to results when loading is complete
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.isLoading && currentQuestionType == QuestionType.TIME_PERIOD) {
                    // Build params from current selections and navigate
                    val params = MatchMapper.toMatchParamsFromChoice(
                        moodNames = state.moodSelected.map { it.name },
                        genreNames = state.genreSelected.map { it.name },
                        timeNames = state.mediaRuntimeSelected.map { it.name },
                        periodNames = state.timePeriodSelected.map { it.name },
                    )
                    val action = MatchQuestionsFragmentDirections
                        .actionMatchQuestionsFragmentToMatchResultsFragment(
                            genres = params.genres.orEmpty(),
                            runtimeGte = params.runtimeGte ?: -1,
                            runtimeLte = params.runtimeLte ?: -1,
                            releaseDateGte = params.releaseDateGte.orEmpty(),
                            releaseDateLte = params.releaseDateLte.orEmpty(),
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun render(state: QuestionUiState) {
        with(binding) {
            currentQuestionType = state.currentQuestionType
            progressIndicator.setProgressCompat(state.progress, true)
            matchQuestionsRv.scrollToPosition(state.currentQuestionType.ordinal)
            binding.isLoading = state.isLoading
            if (state.currentQuestionType == QuestionType.TIME_PERIOD) {
                buttonContinue.setText(R.string.start_matching)
            }
        }
    }
}