package com.example.doggallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doggallery.ui.breedListFactory
import com.example.doggallery.ui.list.BreedListScreen
import com.example.doggallery.ui.list.BreedListViewModel
import com.example.doggallery.ui.theme.DogGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogGalleryTheme {
                val viewModel: BreedListViewModel = viewModel(factory = breedListFactory())
                BreedListScreen(
                    viewModel = viewModel,
                    onBreedClick = {},
                    onFavoritesClick = {}
                )
            }
        }
    }
}