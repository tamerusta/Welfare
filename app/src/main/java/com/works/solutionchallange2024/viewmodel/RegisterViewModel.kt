package com.works.solutionchallange2024.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.works.solutionchallange2024.config.RetrofitClient
import com.works.solutionchallange2024.model.retrofit.NewUser
import com.works.solutionchallange2024.model.retrofit.RegisterResponse
import com.works.solutionchallange2024.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel(){
    private val retrofit = RetrofitClient
    val userApi = retrofit.getClient().create(UserService::class.java)

    fun createUser(newUser: NewUser){
        userApi.createUser(newUser).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val data = response.body()
                if (data?.msg != null)
                {

                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
            }
        })
    }
}