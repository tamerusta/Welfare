package com.works.solutionchallange2024.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.works.solutionchallange2024.config.RetrofitClient
import com.works.solutionchallange2024.model.retrofit.ForgotPassword
import com.works.solutionchallange2024.model.retrofit.UserLogin
import com.works.solutionchallange2024.model.retrofit.UserLoginResponse
import com.works.solutionchallange2024.service.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    interface LoginListener {
        fun onLoginSuccess(token: String)
        fun onLoginFailure(error: String)
    }

    private var loginListener: LoginListener? = null
    val retrofit = RetrofitClient
    private val LoginService = retrofit.getClient().create(AuthService::class.java)
    val errorMessageLiveData = MutableLiveData<String>()

    companion object {
        var token: String? = null
    }

    fun setLoginListener(listener: LoginListener) {
        loginListener = listener

    }
    fun Login(email: String, password: String) {
        val user = UserLogin(email, password)

        LoginService.auth(user).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                val data = response.body()
                if (data?.success == true) {
                    token = data.token
                    Log.e("token", token.toString())
                    loginListener?.onLoginSuccess(token!!)
                    RetrofitClient.setAuthToken(token!!)
                } else {
                    errorMessageLiveData.postValue("Invalid email or password.")
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                errorMessageLiveData.postValue(t.message.toString())
            }
        })
    }
}