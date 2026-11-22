package com.example.doggallery.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface DogApi {

    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("breed_ids") breedId: Int,
        @Query("limit") limit: Int = 20
    ): List<ImageDto>
}