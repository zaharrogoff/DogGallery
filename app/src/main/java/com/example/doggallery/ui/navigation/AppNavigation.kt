package com.example.doggallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.doggallery.ui.breedDetailsFactory
import com.example.doggallery.ui.breedListFactory
import com.example.doggallery.ui.details.BreedDetailsScreen
import com.example.doggallery.ui.details.BreedDetailsViewModel
import com.example.doggallery.ui.favorites.FavoritesScreen
import com.example.doggallery.ui.favorites.FavoritesViewModel
import com.example.doggallery.ui.favoritesFactory
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
                    onBreedClick = { id -> backStack.add(BreedDetailsKey(id)) },
                    onFavoritesClick = { backStack.add(FavoritesKey) }
                )
            }
            entry<BreedDetailsKey> { key ->
                val viewModel: BreedDetailsViewModel = viewModel(
                    key = "details_${key.breedId}",
                    factory = breedDetailsFactory(key.breedId)
                )
                BreedDetailsScreen(
                    viewModel = viewModel,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
            entry<FavoritesKey> {
                val viewModel: FavoritesViewModel = viewModel(factory = favoritesFactory())
                FavoritesScreen(
                    viewModel = viewModel,
                    onBreedClick = { id -> backStack.add(BreedDetailsKey(id)) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}