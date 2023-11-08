package com.example.reto1.ui.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.reto1.data.repository.remote.RemoteUsersDataSource
import com.example.reto1.databinding.LayoutRegisterBinding
import com.example.reto1.utils.Resource

class RegisterActivity: ComponentActivity() {

    private val userRepository = RemoteUsersDataSource()

    private val viewModel: UserViewModel by viewModels { UsersViewModelFactory(
        userRepository
    ) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = LayoutRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.register.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.i("LoginActivity", "Se ha registrado - Observer")
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    Log.i("Gorka_UserActivity", "Register Observer - Success")
                }

                Resource.Status.ERROR -> {
                    Log.i("Gorka_UserActivity", "Register Observer - Toast")
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })



        binding.registerButton.setOnClickListener() {
            val password1 = binding.registerPassword1.text.toString();
            val password2 = binding.registerPassword2.text.toString()
            if (password1 == password2) {
                viewModel.registerUser(
                    binding.registerName.text.toString(),
                    binding.registerSurname.text.toString(),
                    binding.registerEmail.text.toString(),
                    binding.registerLogin.text.toString(),
                    binding.registerPassword1.text.toString()
                )
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("login",binding.registerLogin.text.toString())
                intent.putExtra("password",binding.registerPassword1.text.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,"@String/noequalpassword", Toast.LENGTH_LONG).show()
            }

        }

    }
}