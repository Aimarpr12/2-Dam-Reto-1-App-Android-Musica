package com.example.reto1

import android.content.Context
import android.content.SharedPreferences
import com.example.reto1.ui.users.UserViewModel

class UserPreferences() {
    private val sharedPreferences: SharedPreferences by lazy {
        MyApp.context.getSharedPreferences(MyApp.context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }

    fun saveAuthToken(token: String,id: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(USER_ID,id)
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(USER_TOKEN,null)
    }


    fun fetchUserId(): Int? {
        return sharedPreferences.getInt(USER_ID,0)
    }
}