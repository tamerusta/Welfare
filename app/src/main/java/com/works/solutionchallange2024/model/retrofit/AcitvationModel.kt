package com.works.solutionchallange2024.model.retrofit

data class AcitvationModelRequest(
    val email: String,
)

data class AcitvationModelResponse(
    val success: Boolean,
    val message: String,
    val token: String,
    val userData: UserData,
)

data class AcitvationModelResponse2(
    val message: String,
)
