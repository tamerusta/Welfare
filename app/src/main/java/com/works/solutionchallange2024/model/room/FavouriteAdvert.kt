package com.works.solutionchallange2024.model.room

data class FavouriteAdvert(
    val imageUrl: String,
    val username: String,
    val userRating: Double,
    val point: String,
    val category: String,
    val participants: String,
    val isFavourite: Boolean
)
