package com.example.doggallery.data

import com.example.doggallery.data.local.BreedEntity
import com.example.doggallery.data.remote.toEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val local: BreedLocalDataSource,
    private val remote: BreedRemoteDataSource
) {
    val breeds: Flow<List<BreedEntity>> = local.observeAll()
    val favorites: Flow<List<BreedEntity>> = local.observeFavorites()

    fun observeBreed(id: Int): Flow<BreedEntity?> = local.observeById(id)

    suspend fun getBreed(id: Int): BreedEntity? = local.getById(id)

    /** Загружает породы из сети и сохраняет в Room, не теряя избранное. */
    suspend fun refresh(): Result<Unit> = try {
        val dtos = remote.getBreeds()
        val favoriteIds = local.getFavoriteIds().toSet()
        local.upsertAll(dtos.map { it.toEntity(isFavorite = it.id in favoriteIds) })
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun setFavorite(id: Int, isFavorite: Boolean) = local.setFavorite(id, isFavorite)

    /** Фото породы для галереи. Без сети возвращает хотя бы главное фото. */
    suspend fun loadGallery(breed: BreedEntity): List<String> {
        val urls = try {
            remote.getBreedImages(breed.id).mapNotNull { it.url }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }
        return urls.ifEmpty { listOfNotNull(breed.imageUrl.takeIf { it.isNotBlank() }) }
    }
}