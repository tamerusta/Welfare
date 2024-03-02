package com.works.solutionchallange2024.service

import com.works.solutionchallange2024.model.retrofit.ForgotPassword
import com.works.solutionchallange2024.model.retrofit.ResetPassword
import com.works.solutionchallange2024.model.retrofit.UserLogin
import com.works.solutionchallange2024.model.retrofit.UserLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("auth/login")
    fun auth(@Body user: UserLogin): Call<UserLoginResponse>

    @POST("auth/forgot-password")
    fun forgotPassword(@Body email: String): Call<ForgotPassword>

    @POST("auth/reset-password/{token}")
    fun resetPassword(@Path("token") token: String, @Body request: ResetPassword): Call<ForgotPassword>
}