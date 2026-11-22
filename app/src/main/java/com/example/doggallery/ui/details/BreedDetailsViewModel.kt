package com.example.doggallery.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggallery.data.BreedRepository
import com.example.doggallery.data.local.BreedEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BreedDetailsViewModel(
    private val repository: BreedRepository,
    private val breedId: Int
) : ViewModel() {

    val breed: StateFlow<BreedEntity?> = repository.observeBreed(breedId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _gallery = MutableStateFlow<List<String>>(emptyList())
    val gallery: StateFlow<List<String>> = _gallery.asStateFlow()

    init {
        viewModelScope.launch {
            val entity = repository.getBreed(breedId) ?: return@launch
            _gallery.value = repository.loadGallery(entity)
        }
    }

    fun toggleFavorite() {
        val current = breed.value ?: return
        viewModelScope.launch {
            repository.setFavorite(breedId, !current.isFavorite)
        }
    }
}