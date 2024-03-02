package com.works.solutionchallange2024.service

import com.works.solutionchallange2024.model.retrofit.NewUser
import com.works.solutionchallange2024.model.retrofit.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/register")
    fun createUser(@Body user: NewUser): Call<RegisterResponse>
}