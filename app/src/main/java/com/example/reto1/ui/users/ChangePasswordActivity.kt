package com.example.reto1.ui.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.reto1.MyApp
import com.example.reto1.data.repository.remote.RemoteUsersDataSource
import com.example.reto1.databinding.LayoutChangePasswordBinding
import com.example.reto1.utils.Resource

class ChangePasswordActivity: ComponentActivity() {

    private val userRepository = RemoteUsersDataSource()

    private val viewModel: UserViewModel by viewModels { UsersViewModelFactory(
        userRepository
    ) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = LayoutChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.changePassword.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.i("changePasswordActivity", "Ha entrado - Observer")
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    Log.i("changePasswordActivity", "ChangePassword Observer - Success")
                }

                Resource.Status.ERROR -> {
                    Log.i("changePasswordActivity", "ChangePassword Observer - Toast")
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })



        binding.changePasswordButton.setOnClickListener() {
            val password1 = binding.newPassword.text.toString()
            val password2 = binding.newPassword2.text.toString()
            val currentPassword = binding.password.text.toString()
            val login = MyApp.userPreferences.fetchLogin()
            if (password1 == password2) {
                if (login != null) {
                    viewModel.changePassword(
                        login,
                        password1,
                        currentPassword
                    )
                }
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,"@String/noequalpassword", Toast.LENGTH_LONG).show()
            }

        }

    }
}