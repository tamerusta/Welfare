package com.works.solutionchallange2024.model.room


import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("token")
    val token: String
)