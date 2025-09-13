package com.karrar.movieapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, E> : ViewModel() {

    // Abstract property that subclasses must override
    abstract val initialState: T

    // Use lazy initialization to avoid initialization order issues
    private val _uiState by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<T> by lazy { _uiState.asStateFlow() }

    private val _uiEffect = Channel<E>()
    val uiEffect: Flow<E> = _uiEffect.receiveAsFlow()

    protected fun updateState(transform: (T) -> T) {
        _uiState.update { transform(it) }
    }

    // Add the missing sendEvent method
    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _uiEffect.send(event)
        }
    }

    protected fun <R> launchWithResult(
        action: suspend () -> R,
        onSuccess: (R) -> Unit,
        onError: (String) -> Unit, // Changed to String to match usage
        onStart: () -> Unit = {},
        onFinally: () -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            onStart()
            runCatching { action() }
                .onSuccess(onSuccess)
                .onFailure { throwable ->
                    val errorMessage = throwable.message ?: "Unknown error occurred"
                    onError(errorMessage)
                }
            onFinally()
        }
    }

    protected fun launchAndForget(
        action: suspend () -> Unit,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit,
        onStart: () -> Unit = {},
        onFinally: () -> Unit = {}
    ) = viewModelScope.launch(Dispatchers.IO) {
        onStart()
        runCatching { action() }
            .onSuccess { onSuccess() }
            .onFailure { throwable ->
                val errorMessage = throwable.message ?: "Unknown error occurred"
                onError(errorMessage)
            }
        onFinally()
    }

    protected fun <R> launchWithFlow(
        flowAction: suspend () -> Flow<R>,
        onSuccess: (R) -> Unit,
        onError: (String) -> Unit,
        onStart: () -> Unit = {},
        onFinally: () -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            onStart()
            try {
                flowAction().collect { result ->
                    onSuccess(result)
                }
            } catch (throwable: Throwable) {
                val errorMessage = throwable.message ?: "Unknown error occurred"
                onError(errorMessage)
            } finally {
                onFinally()
            }
        }
    }

    protected fun wrapWithState(
        function: suspend () -> Unit,
        errorFunction: (throwable: Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                function()
            } catch (throwable: Throwable) {
                errorFunction(throwable)
            }
        }
    }

    protected fun <DataType> collectData(
        data: Flow<List<DataType>>,
        function: suspend (List<DataType>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            data.collect { dataList ->
                function(dataList)
            }
        }
    }

    abstract fun getData()
}