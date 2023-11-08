package com.example.reto1

import android.app.Application
import android.content.Context

class MyApp : Application() {

    companion object {
        lateinit var context: Context;
        lateinit var userPreferences: UserPreferences;
    }

    override fun onCreate() {
        super.onCreate()
        context = this;
        userPreferences = UserPreferences()
    }
}