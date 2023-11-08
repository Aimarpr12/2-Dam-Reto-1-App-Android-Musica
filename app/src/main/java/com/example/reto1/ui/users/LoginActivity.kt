package com.example.reto1.ui.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.reto1.MyApp
import com.example.reto1.data.repository.remote.RemoteUsersDataSource
import com.example.reto1.databinding.LayoutLoginBinding
import com.example.reto1.ui.songs.SongActivity
import com.example.reto1.utils.Resource

class LoginActivity: ComponentActivity() {

    private val userRepository = RemoteUsersDataSource()

    private val viewModel: UserViewModel by viewModels { UsersViewModelFactory(
        userRepository
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = LayoutLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null) {
            val sessionLogin = intent.getStringExtra("login")
            val sessionPassword = intent.getStringExtra("password")
            binding.login.setText(sessionLogin)
            binding.loginPassword.setText(sessionPassword)
        }

        if (MyApp.userPreferences.fetchPassword() != null) {
            binding.login.setText(MyApp.userPreferences.fetchLogin())
            binding.loginPassword.setText(MyApp.userPreferences.fetchPassword())
            binding.rememberMe.isChecked = true
        }

        viewModel.login.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.i("LoginActivity", "Se ha logeado - Observer")
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.i("Gorka_UserActivity", "Login Observer - Success")
                    it.data?.let { data ->
                        MyApp.userPreferences.saveAuthToken(data.accessToken,data.id.toInt(),data.login)
                        val intent = Intent(this, SongActivity::class.java).apply {
                            // putExtra(EXTRA_MESSAGE, message)
                        }
                        startActivity(intent)
                        finish()

                    }
                }

                Resource.Status.ERROR -> {
                    binding.buttonChangePass.visibility = View.VISIBLE
                    Log.i("Gorka_UserActivity", "Login Observer - Toast")
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })

        binding.buttonChangePass.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.setOnClickListener() {

            val login = binding.login.text.toString()
            val password = binding.loginPassword.text.toString()
            viewModel.loginUser(
                login,
                password
            )
            binding.login.setText("");
            binding.loginPassword.setText("");
            if (binding.rememberMe.isChecked) {
                MyApp.userPreferences.saveRememberMe(password)
            } else {
                if (MyApp.userPreferences.fetchPassword() != null) {
                    MyApp.userPreferences.removeRememberMe()
                }
            }
        }

        binding.registerButton.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}

