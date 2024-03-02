package com.works.solutionchallange2024.model.retrofit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserCredentials(
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
    val phone: String,
) : Parcelable


