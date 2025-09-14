package com.karrar.movieapp.domain.usecases.match

import com.karrar.movieapp.domain.models.Genre
import com.karrar.movieapp.domain.usecases.GenreUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.match.MatchEvent
import com.karrar.movieapp.ui.match.MatchInteractionListener
import com.karrar.movieapp.ui.match.MatchMapper
import com.karrar.movieapp.ui.match.MatchPages
import com.karrar.movieapp.ui.match.MatchUiState
import com.karrar.movieapp.ui.match.QuestionType
import com.karrar.movieapp.ui.match.QuestionUiState
import com.moscow.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val getMatchedMovies: GetMatchedMovies,
    private val genreUseCase: GenreUseCase,
) : BaseViewModel<MatchUiState, MatchEvent>(MatchUiState()),
    MatchInteractionListener {


    override fun getData() {
        getGenres()
    }


    private fun getGenres() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        launchWithResult(
            action = { genreUseCase.getMoviesGenres() },
            onSuccess = { genres: List<Genre> ->                updateState {
                    it.copy(movieGenre = genres.map { genre -> genre.toUi() })
                }
                loadMatchData()
            },
            onError = { errorMessage ->
                updateState {
                    it.copy(
                        isLoading = false,
                        shouldShowError = true,
                    )
                }
            },
        )
    }

    override fun onClickStartMatching() {
        updateState { it.copy(currentPage = MatchPages.QuestionsPage) }
    }

    override fun onClickFinishMatching() {
        updateState { it.copy(currentPage = MatchPages.ResultsPage) }
    }

    override fun onClickNextQuestion() {
        if (uiState.value.isNextButtonActivated)
            updateState { state ->
                val nextIndex = state.currentQuestionType.ordinal + 1
                QuestionType.entries.getOrNull(nextIndex)
                    ?: run {
                        getGenres()
                        return@updateState state.copy(isLoadingRecommendations = true)
                    }

                state.copy(currentQuestionType = QuestionType.entries[nextIndex])
            }
    }

    // REMOVED DUPLICATE - Keep only this one
    override fun onAnswerSelected(
        type: QuestionType,
        answer: QuestionUiState
    ) {
        updateState { state ->
            when (type) {
                QuestionType.MOOD -> if (state.currentQuestionType == QuestionType.MOOD) {
                    state.copy(
                        moodQuestions = state.moodQuestions.map {
                            if (it.id == answer.id) it.copy(isSelected = !it.isSelected) else it
                        }
                    )
                } else state

                QuestionType.GENRE -> if (state.currentQuestionType == QuestionType.GENRE) state.copy(
                    genreQuestions = state.genreQuestions.map {
                        if (it.id == answer.id) it.copy(isSelected = !it.isSelected) else it
                    }
                ) else state

                QuestionType.TIME -> if (state.currentQuestionType == QuestionType.TIME) state.copy(
                    timeQuestions = state.timeQuestions.map {
                        if (it.id == answer.id) it.copy(isSelected = !it.isSelected) else
                            it.copy(isSelected = false)
                    }
                ) else state

                QuestionType.TYPE -> if (state.currentQuestionType == QuestionType.TYPE) state.copy(
                    movieTypeQuestions = state.movieTypeQuestions.map {
                        if (it.id == answer.id) it.copy(isSelected = !it.isSelected) else
                            it.copy(isSelected = false)
                    }
                ) else state
            }
        }
    }

    private fun loadMatchData() {
        launchWithResult(
            action = {
                val params = MatchMapper.toMatchParams(uiState.value)
                getMatchedMovies(
                    page = 1,
                    genres = params.genres,
                    runtimeGte = params.runtimeGte,
                    runtimeLte = params.runtimeLte,
                    releaseDateGte = params.releaseDateGte,
                    releaseDateLte = params.releaseDateLte
                )
            },
            onSuccess = { onLoadMatchDataSuccess(it) },
            onError = { onLoadMatchDataError(it) }
        )
    }

    private fun onLoadMatchDataSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                matchResults = movies.take(5).map { movie ->
                    MatchMapper.toMatchUiState(
                        movie = movie,
                        genres = uiState.value.movieGenre
                    )
                },
                isLoadingRecommendations = false,
                currentPage = MatchPages.ResultsPage
            )
        }
    }

    private fun onLoadMatchDataError(errorMessage: String) {
        updateState {
            it.copy(
                isLoadingRecommendations = false,
                shouldShowError = true
            )
        }
    }

    override fun onNavigateBack() {
        when (uiState.value.currentPage) {
            MatchPages.QuestionsPage -> {
                updateState { state ->
                    state.copy(
                        currentPage = if (state.currentQuestionType == QuestionType.MOOD)
                            MatchPages.StartPage
                        else
                            MatchPages.QuestionsPage,
                        currentQuestionType = QuestionType.entries[state.currentQuestionType.ordinal.minus(
                            1
                        ).coerceAtLeast(0)]
                    )
                }
            }

            MatchPages.ResultsPage -> updateState {
                it.copy(
                    currentPage = MatchPages.StartPage,
                    currentQuestionType = QuestionType.MOOD,
                    moodQuestions = getMoodQuestionAnswers(),
                    genreQuestions = getGenreQuestionAnswers(),
                    timeQuestions = getTimeQuestionAnswers(),
                    movieTypeQuestions = getMovieTypeQuestionAnswers()
                )
            }

            else -> {}
        }
    }

    override fun onMovieClick(id: Int) {
        sendEvent(MatchEvent.OnMovieClick(id = id))
    }

    override fun onSaveClick(id: Int) {
        sendEvent(MatchEvent.AddToCollection(id = id))
    }


    override fun onRetry() {
        getGenres()
        updateState { it.copy(shouldShowError = false, errorMessage = null) }
    }

    // ADD THESE MISSING HELPER METHODS
    private fun getMoodQuestionAnswers(): List<QuestionUiState> {
        return listOf(
            QuestionUiState(1, "Happy", "",false),
            QuestionUiState(2, "Sad", "false"),
            QuestionUiState(3, "Excited", "false"),
            QuestionUiState(4, "Relaxed", "false")
        )
    }

    private fun getGenreQuestionAnswers(): List<QuestionUiState> {
        return listOf(
            QuestionUiState(1, "Action", "false"),
            QuestionUiState(2, "Comedy", "false"),
            QuestionUiState(3, "Drama", "false"),
            QuestionUiState(4, "Horror", "false")
        )
    }

    private fun getTimeQuestionAnswers(): List<QuestionUiState> {
        return listOf(
            QuestionUiState(1, "Short (< 90 min)", "false"),
            QuestionUiState(2, "Medium (90-120 min)", "false"),
            QuestionUiState(3, "Long (> 120 min)", "false")
        )
    }

    private fun getMovieTypeQuestionAnswers(): List<QuestionUiState> {
        return listOf(
            QuestionUiState(1, "Popular", "false"),
            QuestionUiState(2, "Recent", "false"),
            QuestionUiState(3, "Classic", "false"),
            QuestionUiState(4, "Hidden Gems", "false")
        )
    }
}