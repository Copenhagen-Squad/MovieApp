package com.karrar.movieapp.ui.match.questions

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MatchQuestionsViewModel @Inject constructor() :
    BaseViewModel(),
    QuestionInteractionListener {

    private val _uiState = MutableStateFlow(QuestionUiState())
    val uiState = _uiState.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val items = mutableListOf<Question>()

    override fun onNextClicked() {
        val currentType = _uiState.value.currentQuestionType

        if (getSelectedChoices(currentType).isEmpty()) return

        updateCurrentQuestion(currentType)

        if (currentType == QuestionType.TIME_PERIOD) {
            _uiState.update { it.copy(isLoading = true) }
            return
        }

        val nextType = when (currentType) {
            QuestionType.MOOD -> QuestionType.GENRE
            QuestionType.GENRE -> QuestionType.MEDIA_RUNTIME
            QuestionType.MEDIA_RUNTIME -> QuestionType.TIME_PERIOD
            QuestionType.TIME_PERIOD -> return
        }

        _questions.update { currentList ->
            val updatedList = currentList.toMutableList()
            val nextQuestion = items.firstOrNull { it.type == nextType }
            if (nextQuestion != null && updatedList.none { it.type == nextType }) {
                updatedList.add(nextQuestion)
            }
            updatedList
        }

        _uiState.update { state ->
            state.copy(
                currentQuestionType = nextType,
                progress = state.progress + Constants.PROGRESS_VALUE,
            )
        }
    }


    private fun updateCurrentQuestion(type: QuestionType) {
        _questions.update { list ->
            list.map { q ->
                if (q.type == type) {
                    val selectedChoices = getSelectedChoices(type)
                    val updatedChoices = q.choices.map { choice ->
                        choice.copy(isSelected = selectedChoices.any { it.nameRes == choice.nameRes })
                    }
                    q.copy(
                        choices = updatedChoices,
                        isAnswered = selectedChoices.isNotEmpty()
                    )
                } else q
            }
        }
    }

    private fun getSelectedChoices(type: QuestionType): List<Choice> =
        when (type) {
            QuestionType.MOOD -> _uiState.value.moodSelected
            QuestionType.GENRE -> _uiState.value.genreSelected
            QuestionType.MEDIA_RUNTIME -> _uiState.value.mediaRuntimeSelected
            QuestionType.TIME_PERIOD -> _uiState.value.timePeriodSelected
        }

    override fun getData() {
        items.clear()

        val mood = listOf(
            Choice(nameRes = R.string.choice_chill, icon = R.drawable.ic_headphone_duetone),
            Choice(nameRes = R.string.choice_excited, icon = R.drawable.ic_flame_duetone),
            Choice(nameRes = R.string.choice_emotional, icon = R.drawable.ic_heart_duetone),
            Choice(nameRes = R.string.choice_curious, icon = R.drawable.ic_search_duetone),
        )

        val genre = listOf(
            Choice(nameRes = R.string.choice_action),
            Choice(nameRes = R.string.choice_comedy),
            Choice(nameRes = R.string.choice_drama),
            Choice(nameRes = R.string.choice_romance),
            Choice(nameRes = R.string.choice_scifi),
            Choice(nameRes = R.string.choice_thriller),
            Choice(nameRes = R.string.choice_animation),
            Choice(nameRes = R.string.choice_mystery),
        )

        val time = listOf(
            Choice(
                nameRes = R.string.choice_time_short,
                descriptionRes = R.string.choice_time_short_desc,
                icon = R.drawable.ic_time_short_duetone,
            ),
            Choice(
                nameRes = R.string.choice_time_medium,
                descriptionRes = R.string.choice_time_medium_desc,
                icon = R.drawable.ic_time_medium_duetone,
            ),
            Choice(
                nameRes = R.string.choice_time_long,
                descriptionRes = R.string.choice_time_long_desc,
                icon = R.drawable.ic_time_long_duetone,
            ),
        )

        val release = listOf(
            Choice(nameRes = R.string.choice_release_recent),
            Choice(nameRes = R.string.choice_release_classic),
            Choice(nameRes = R.string.choice_release_both),
        )

        items.addAll(
            listOf(
                Question(
                    question = R.string.question_mood,
                    type = QuestionType.MOOD,
                    choices = mood,
                    isAnswered = false,
                ),
                Question(
                    question = R.string.question_genre,
                    type = QuestionType.GENRE,
                    choices = genre,
                    isAnswered = false,
                ),
                Question(
                    question = R.string.question_length,
                    type = QuestionType.MEDIA_RUNTIME,
                    choices = time,
                    isAnswered = false,
                ),
                Question(
                    question = R.string.question_release,
                    type = QuestionType.TIME_PERIOD,
                    choices = release,
                    isAnswered = false,
                ),
            ),
        )

        _questions.value = listOf(items.first())
    }

    fun onChoiceSelected(
        type: QuestionType,
        choices: List<Choice>,
    ) {
        _uiState.update { state ->
            when (type) {
                QuestionType.MOOD -> state.copy(moodSelected = choices)
                QuestionType.GENRE -> state.copy(genreSelected = choices)
                QuestionType.MEDIA_RUNTIME -> state.copy(mediaRuntimeSelected = choices)
                QuestionType.TIME_PERIOD -> state.copy(timePeriodSelected = choices)
            }
        }
    }

    override fun getCurrentQuestionType(): QuestionType =
        _uiState.value.currentQuestionType
}