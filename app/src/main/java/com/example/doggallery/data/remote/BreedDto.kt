package com.example.doggallery.data.remote

import com.example.doggallery.data.local.BreedEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedDto(
    val id: Int,
    val name: String,
    val temperament: String?,
    val origin: String?,
    @Json(name = "life_span") val lifeSpan: String?,
    val description: String?,
    @Json(name = "bred_for") val bredFor: String?,
    @Json(name = "reference_image_id") val referenceImageId: String?,
    val image: ImageDto?
)

@JsonClass(generateAdapter = true)
data class ImageDto(
    val url: String?
)

fun BreedDto.toEntity(isFavorite: Boolean): BreedEntity {
    val fallbackImage = referenceImageId?.let { "https://cdn2.thedogapi.com/images/$it.jpg" }
    return BreedEntity(
        id = id,
        name = name,
        temperament = temperament.orEmpty(),
        origin = origin?.takeIf { it.isNotBlank() },
        lifeSpan = lifeSpan.orEmpty(),
        description = description ?: bredFor,
        imageUrl = image?.url ?: fallbackImage ?: "",
        isFavorite = isFavorite
    )
}