package com.works.solutionchallange2024

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.works.solutionchallange2024.config.RetrofitClient
import com.works.solutionchallange2024.databinding.ActivityHomeBinding
import com.works.solutionchallange2024.manager.AppPref
import android.util.Log
import com.works.solutionchallange2024.model.retrofit.UserLogin
import com.works.solutionchallange2024.model.retrofit.UserLoginResponse
import com.works.solutionchallange2024.service.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val appPref = AppPref(this)
        val email = appPref.getMail()
        val password = appPref.getPassword()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.homeActivityBottomNav, navHostFragment.navController)

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val userLogin = UserLogin(email, password)
            login(userLogin)
        }

    }

    private fun login(userLogin: UserLogin) {
        val clientService = RetrofitClient.getClient().create(AuthService::class.java)

        clientService.auth(userLogin).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                val data = response.body()

                if (data?.success == true) {
                    val token = data.token
                    RetrofitClient.setAuthToken(token)
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as NavHostFragment
                    NavigationUI.setupWithNavController(binding.homeActivityBottomNav,navHostFragment.navController)
                } else {
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
            }
        })
    }



    fun hideBottomNavigation() {
        binding.homeActivityBottomNav.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.homeActivityBottomNav.visibility = View.VISIBLE
    }


}
