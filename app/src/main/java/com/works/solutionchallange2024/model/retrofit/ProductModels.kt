package com.works.solutionchallange2024.model.retrofit

data class ProductData(
    val id: Long,
    val category: String,
    val tag: String,
    val city: String,
    val latitude: String,
    val longitude: String,
    val description: String,
    val address: String
)

data class UpdateProductData(
    val category: String,
    val tag: String,
    val city: String,
    val latitude: String,
    val longitude: String,
    val description: String,
    val address: String
)

data class TagList(
    val data: List<Tag>
)

data class Tag(
    val category: String,
    val tag: String,
)



