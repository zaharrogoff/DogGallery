package com.example.doggallery.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doggallery.AppDependencies
import com.example.doggallery.ui.details.BreedDetailsViewModel
import com.example.doggallery.ui.list.BreedListViewModel

fun breedListFactory() = viewModelFactory {
    initializer { BreedListViewModel(AppDependencies.repository) }
}

fun breedDetailsFactory(breedId: Int) = viewModelFactory {
    initializer { BreedDetailsViewModel(AppDependencies.repository, breedId) }
}