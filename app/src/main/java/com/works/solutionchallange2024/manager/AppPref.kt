package com.works.solutionchallange2024.manager

import android.content.Context
import android.content.SharedPreferences

class AppPref(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Login", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val mail_KEY = "mail"
        private const val password_KEY = "password"
        private const val isChecked_KEY = "isChecked"
        private const val token_KEY = "token"
    }

    fun setIsChecked(isChecked: Boolean) {
        editor.apply {
            putBoolean(isChecked_KEY, isChecked)
            apply()
        }
    }


    fun userData(mail: String?, password: String, isChecked: Boolean) {
        editor.apply {
            putString(mail_KEY, mail)
            putString(password_KEY, password)
            putBoolean(isChecked_KEY, isChecked)
            apply()
        }
    }

    fun saveToken(token: String) {
        editor.apply {
            putString(token_KEY, token)
            apply()
        }
    }

    fun clearToken() {
        editor.apply {
            editor.remove(token_KEY)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(token_KEY, null)
    }

    fun clearData() {
        editor.apply {
            editor.remove(mail_KEY)
            editor.remove(password_KEY)
            editor.putBoolean(isChecked_KEY, false)
            apply()
        }
    }

    fun getIsChecked(): Boolean {
        return sharedPreferences.getBoolean(isChecked_KEY, false)
    }

    fun getMail(): String? {
        return sharedPreferences.getString(mail_KEY, null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString(password_KEY, null)
    }
}