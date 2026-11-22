package com.example.doggallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.doggallery.ui.breedListFactory
import com.example.doggallery.ui.list.BreedListScreen
import com.example.doggallery.ui.list.BreedListViewModel

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(BreedListKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<BreedListKey> {
                val viewModel: BreedListViewModel = viewModel(factory = breedListFactory())
                BreedListScreen(
                    viewModel = viewModel,
                    onBreedClick = {},
                    onFavoritesClick = {}
                )
            }
        }
    )
}