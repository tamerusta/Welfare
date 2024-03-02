package com.works.solutionchallange2024.config

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val Base_URL = "http://app.welfare.ws/api/v1/"
    private var retrofit: Retrofit? = null
    private var authToken: String? = null

    val client = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            var newRequest = originalRequest
            authToken?.let { token ->
                newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            }
            chain.proceed(newRequest)
        }
        .build()

    fun setAuthToken(token: String) {
        authToken = token
    }

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit
                .Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        return retrofit as Retrofit
    }
}