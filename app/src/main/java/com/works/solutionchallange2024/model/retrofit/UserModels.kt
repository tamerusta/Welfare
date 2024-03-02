package com.works.solutionchallange2024.model.retrofit

data class UserData(
    val _id: String,
    val username: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val city: String,
    val location: String,
    val latitude: String,
    val longitude: String,
    val isVerrified: Boolean,
)

data class NewUser(
    val email: String,
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val location: String,
    val city: String,
    val longitude: String,
    val latitude: String
)

data class UserLogin(
    val email: String,
    val password: String
)

data class UserLoginResponse(
    val success: Boolean,
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val username: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val city: String,
    val location: String,
    val longitude: String,
    val latitude: String,
    val isVerified: Boolean,
    val v: Long,
    val actionsHistory: List<Any?>,
    val participatedAdverts: List<Any?>,
    val points: Long,
    val wonAdverts: List<Any?>,
    val lostAdverts: List<Any?>,
    val resetPasswordExpire: String,
    val resetPasswordToken: String
)

data class RegisterResponse(
    val msg: String,
)

data class ForgotPassword(
    val message: String,
)

data class ResetPassword(
    val password: String,
    val confirmPassword: String,
)