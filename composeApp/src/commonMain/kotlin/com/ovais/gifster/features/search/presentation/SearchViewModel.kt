package com.ovais.gifster.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.gifster.core.data.network.onError
import com.ovais.gifster.core.data.network.onSuccess
import com.ovais.gifster.features.home.presentation.HomeUiState
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
        _currentPage.value = currentPage.value + 1
        _uiState.update { state ->
            when (state) {
                is SearchUiState.Success -> {
                    state.copy(
                        isLoadingMore = true
                    )
                }

                else -> state
            }
        }
        viewModelScope.launch {
            search(queryFlow.value)
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
                        _uiState.value = SearchUiState.Idle
                    } else {
                        search(query)
                    }
                }
        }
    }

    private suspend fun search(query: String) {
        _uiState.value = SearchUiState.Loading(query)
        try {
            val param = SearchRequestParam(
                offset = calculateOffset(pageNumber = currentPage.value),
                query = query
            )
            searchGifUseCase(param).onSuccess { result ->
                if (result.isEmpty()) {
                    _uiState.value = SearchUiState.Empty(query)
                } else {
                    _uiState.value = SearchUiState.Success(query, result, isLoadingMore = false)
                }
            }.onError {

            }

        } catch (e: Exception) {
            _uiState.value = SearchUiState.Error(
                query = query,
                message = e.message ?: "Something went wrong"
            )
        }
    }
}