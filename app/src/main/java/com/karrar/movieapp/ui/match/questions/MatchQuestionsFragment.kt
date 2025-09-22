package com.karrar.movieapp.ui.match.questions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchQuestionsBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.match.questions.adapter.QuestionAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint
import com.karrar.movieapp.ui.match.questions.QuestionType

@AndroidEntryPoint
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
        setTitle(true, getString(R.string.discover_your_match))
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
                    // Simulate loading time and navigate to results
                    kotlinx.coroutines.delay(2000)
                    findNavController().navigate(R.id.action_matchQuestionsFragment_to_matchResultsFragment)
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