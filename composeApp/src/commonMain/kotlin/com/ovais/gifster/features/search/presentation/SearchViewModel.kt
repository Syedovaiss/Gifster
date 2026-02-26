package com.ovais.gifster.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.gifster.core.data.network.onError
import com.ovais.gifster.core.data.network.onSuccess
import com.ovais.gifster.features.search.data.SearchRequestParam
import com.ovais.gifster.features.search.domain.SearchGifUseCase
import com.ovais.gifster.utils.calculateOffset
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchGifUseCase: SearchGifUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    private val queryFlow = MutableStateFlow("")
    private val _currentPage = MutableStateFlow(1)
    private val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    init {
        observeQuery()
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.OnQueryChanged -> {
                queryFlow.value = intent.query
            }

            is SearchIntent.Retry -> {
                val current = _uiState.value
                if (current is SearchUiState.Error) {
                    viewModelScope.launch { search(current.query) }
                }
            }

            is SearchIntent.LoadMore -> {
                loadMore()
            }
        }
    }

    private fun loadMore() {
        val state = _uiState.value

        if (state is SearchUiState.Success && !state.isLoadingMore) {
            _currentPage.value += 1

            viewModelScope.launch {
                search(state.query)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->

                    if (query.isBlank()) {
                        _currentPage.value = 1
                        _uiState.value = SearchUiState.Idle
                        return@collectLatest
                    }
                    _currentPage.value = 1

                    search(query)
                }
        }
    }

    private suspend fun search(query: String) {

        val isFirstPage = currentPage.value == 1

        // 1️⃣ Only show full Loading on first page
        if (isFirstPage) {
            _uiState.value = SearchUiState.Loading(query)
        } else {
            _uiState.update { state ->
                if (state is SearchUiState.Success) {
                    state.copy(isLoadingMore = true)
                } else state
            }
        }

        try {
            val param = SearchRequestParam(
                offset = calculateOffset(pageNumber = currentPage.value),
                query = query
            )

            searchGifUseCase(param)
                .onSuccess { result ->

                    if (result.isEmpty() && isFirstPage) {
                        _uiState.value = SearchUiState.Empty(query)
                        return@onSuccess
                    }

                    _uiState.update { state ->
                        when (state) {

                            is SearchUiState.Success -> {
                                state.copy(
                                    results = if (isFirstPage) {
                                        result
                                    } else {
                                        state.results + result
                                    },
                                    isLoadingMore = false
                                )
                            }

                            // First successful load
                            is SearchUiState.Loading -> {
                                SearchUiState.Success(
                                    query = query,
                                    results = result,
                                    isLoadingMore = false
                                )
                            }

                            else -> state
                        }
                    }
                }

                .onError { error ->

                    _uiState.update { state ->
                        when (state) {
                            is SearchUiState.Success -> {
                                state.copy(isLoadingMore = false)
                            }

                            else -> SearchUiState.Error(
                                query = query,
                                message = "Something went wrong"
                            )
                        }
                    }
                }

        } catch (e: Exception) {
            _uiState.update { state ->
                when (state) {
                    is SearchUiState.Success -> {
                        state.copy(isLoadingMore = false)
                    }

                    else -> SearchUiState.Error(
                        query = query,
                        message = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }
}