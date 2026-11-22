package com.example.doggallery.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds ORDER BY name")
    fun observeAll(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1 ORDER BY name")
    fun observeFavorites(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE id = :id")
    fun observeById(id: Int): Flow<BreedEntity?>

    @Query("SELECT * FROM breeds WHERE id = :id")
    suspend fun getById(id: Int): BreedEntity?

    @Query("SELECT id FROM breeds WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Int>

    @Upsert
    suspend fun upsertAll(breeds: List<BreedEntity>)

    @Query("UPDATE breeds SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Int, isFavorite: Boolean)
}