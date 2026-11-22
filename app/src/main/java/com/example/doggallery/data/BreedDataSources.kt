package com.example.doggallery.data

import com.example.doggallery.data.local.BreedDao
import com.example.doggallery.data.local.BreedEntity
import com.example.doggallery.data.remote.BreedDto
import com.example.doggallery.data.remote.DogApi
import com.example.doggallery.data.remote.ImageDto
import kotlinx.coroutines.flow.Flow

class BreedLocalDataSource(private val dao: BreedDao) {
    fun observeAll(): Flow<List<BreedEntity>> = dao.observeAll()
    fun observeFavorites(): Flow<List<BreedEntity>> = dao.observeFavorites()
    fun observeById(id: Int): Flow<BreedEntity?> = dao.observeById(id)
    suspend fun getById(id: Int): BreedEntity? = dao.getById(id)
    suspend fun getFavoriteIds(): List<Int> = dao.getFavoriteIds()
    suspend fun upsertAll(breeds: List<BreedEntity>) = dao.upsertAll(breeds)
    suspend fun setFavorite(id: Int, isFavorite: Boolean) = dao.setFavorite(id, isFavorite)
}

class BreedRemoteDataSource(private val api: DogApi) {
    suspend fun getBreeds(): List<BreedDto> = api.getBreeds()
    suspend fun getBreedImages(breedId: Int): List<ImageDto> = api.getBreedImages(breedId)
}