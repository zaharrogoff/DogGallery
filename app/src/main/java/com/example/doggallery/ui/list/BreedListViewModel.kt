package com.example.doggallery.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggallery.data.BreedRepository
import com.example.doggallery.data.local.BreedEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BreedListUiState(
    val breeds: List<BreedEntity> = emptyList(),
    val totalCount: Int = 0,
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

private data class LoadState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class BreedListViewModel(private val repository: BreedRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val loadState = MutableStateFlow(LoadState(isLoading = true))

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events: SharedFlow<String> = _events.asSharedFlow()

    val uiState: StateFlow<BreedListUiState> = combine(
        repository.breeds,
        _query,
        loadState
    ) { all, query, load ->
        val text = query.trim()
        val filtered = if (text.isEmpty()) all
        else all.filter { it.name.contains(text, ignoreCase = true) }

        BreedListUiState(
            breeds = filtered,
            totalCount = all.size,
            isInitialLoading = load.isLoading && all.isEmpty(),
            isRefreshing = load.isLoading && all.isNotEmpty(),
            errorMessage = load.error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BreedListUiState()
    )

    init {
        refresh()
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun refresh() {
        viewModelScope.launch {
            loadState.value = LoadState(isLoading = true)
            val result = repository.refresh()
            val error = result.exceptionOrNull()?.let {
                "Не удалось обновить данные. Проверьте подключение к сети."
            }
            loadState.value = LoadState(isLoading = false, error = error)
            error?.let { _events.tryEmit(it) }
        }
    }

    fun toggleFavorite(breed: BreedEntity) {
        viewModelScope.launch {
            repository.setFavorite(breed.id, !breed.isFavorite)
        }
    }
}