package com.example.doggallery.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggallery.data.BreedRepository
import com.example.doggallery.data.local.BreedEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: BreedRepository) : ViewModel() {

    val favorites: StateFlow<List<BreedEntity>?> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun toggleFavorite(breed: BreedEntity) {
        viewModelScope.launch {
            repository.setFavorite(breed.id, !breed.isFavorite)
        }
    }
}