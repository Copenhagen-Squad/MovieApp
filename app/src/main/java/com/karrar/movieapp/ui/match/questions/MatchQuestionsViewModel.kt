package com.karrar.movieapp.ui.match.questions

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseViewModel
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
            val total = items.size
            val answered = _questions.value.count { it.isAnswered }
            state.copy(
                currentQuestionType = nextType,
                progress = (answered * 100) / total
            )
        }
    }

    private fun updateCurrentQuestion(type: QuestionType) {
        _questions.update { list ->
            list.map { q ->
                if (q.type == type) {
                    val selected = getSelectedChoices(type)
                    q.copy(
                        choices = selected.map { it.copy(isSelected = true) },
                        isAnswered = true
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
            Choice(name = "Chill", icon = R.drawable.ic_headphone_duetone),
            Choice(name = "Excited", icon = R.drawable.ic_flame_duetone),
            Choice(name = "Emotional", icon = R.drawable.ic_heart_duetone),
            Choice(name = "Curious", icon = R.drawable.ic_search_duetone),
        )

        val genre = listOf(
            Choice(name = "Action"),
            Choice(name = "Comedy"),
            Choice(name = "Drama"),
            Choice(name = "Romance"),
            Choice(name = "Sci-Fi"),
            Choice(name = "Thriller"),
            Choice(name = "Animation"),
            Choice(name = "Mystery"),
        )

        val time = listOf(
            Choice(
                name = "Short",
                description = "(Under 90 min)",
                icon = R.drawable.ic_time_short_duetone,
            ),
            Choice(
                name = "Medium",
                description = "(between 90 & 120 min)",
                icon = R.drawable.ic_time_medium_duetone,
            ),
            Choice(
                name = "Long",
                description = "(Over 120 min)",
                icon = R.drawable.ic_time_long_duetone,
            ),
        )

        val release = listOf(
            Choice(name = "Recent"),
            Choice(name = "Classic"),
            Choice(name = "Both"),
        )

        items.addAll(
            listOf(
                Question(
                    question = "What mood are you in?",
                    type = QuestionType.MOOD,
                    choices = mood,
                    isAnswered = false,
                ),
                Question(
                    question = "What genre are you interested in?",
                    type = QuestionType.GENRE,
                    choices = genre,
                    isAnswered = false,
                ),
                Question(
                    question = "Preferred movie length?",
                    type = QuestionType.MEDIA_RUNTIME,
                    choices = time,
                    isAnswered = false,
                ),
                Question(
                    question = "Do you prefer recent or classic movies?",
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