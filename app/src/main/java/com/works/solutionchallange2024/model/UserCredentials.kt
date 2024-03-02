package com.works.solutionchallange2024.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserCredentials(
    val fullname: String,
    val username: String,
    val email: String,
    val phone: String,
) : Parcelable