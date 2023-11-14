package com.example.reto1.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.example.reto1.R
import com.example.reto1.ui.users.LoginActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logo_layout)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, LoginActivity::class.java).apply {

                }
                startActivity(intent)
                finish()
            },
            2000 // value in milliseconds
        )

    }
}