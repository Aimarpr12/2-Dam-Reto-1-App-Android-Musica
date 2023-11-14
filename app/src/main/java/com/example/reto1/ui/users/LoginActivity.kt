package com.example.reto1.ui.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.Observer
import com.example.reto1.MyApp
import com.example.reto1.R
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

        //Se comprueba si hay datos extra introducidos en el intent
        val extras = intent.extras
        if (extras != null) {
            //Recoge los datos y los coloca en los EditText
            var sessionLogin = intent.getStringExtra("login")
            var sessionPassword = intent.getStringExtra("password")
            binding.login.setText(sessionLogin)
            binding.loginPassword.setText(sessionPassword)
        }

        //Comprueba si esta la contraseña almacenada en el userPreferences
        if (MyApp.userPreferences.fetchPassword() != null) {
            //Rellena el login, la contraseña y selecciona el recuerdame.
            binding.login.setText(MyApp.userPreferences.fetchLogin())
            binding.loginPassword.setText(MyApp.userPreferences.fetchPassword())
            binding.rememberMe.isChecked = true
        }

        //Comprueba si se selecciona el boton de Login
        viewModel.login.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    //En caso de SUCCESS, introduce el id, login y el token en userPreferences
                    it.data?.let { data ->
                        MyApp.userPreferences.saveAuthToken(data.accessToken,data.id.toInt(),data.login)
                        val intent = Intent(this, SongActivity::class.java).apply {
                            // putExtra(EXTRA_MESSAGE, message)
                        }
                        binding.buttonChangePass.visibility = View.INVISIBLE
                        startActivity(intent)
                        finish()

                    }
                }

                Resource.Status.ERROR -> {
                    binding.buttonChangePass.visibility = View.VISIBLE
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })

        binding.loginButton.setOnClickListener() {

            //Recoge los datos
            val login = binding.login.text.toString()
            val password = binding.loginPassword.text.toString()

            //Comprueba que contengan datos
            if (login.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(
                    login,
                    password
                )
                binding.login.setText("");
                binding.loginPassword.setText("");
                //Si el checkbox esta seleccionado, guarda los datos en userPreferences
                if (binding.rememberMe.isChecked) {
                    MyApp.userPreferences.saveRememberMe(password)
                } else {
                    //Como el checkbox no esta seleccionado, borra la contraseña del userPreferences
                    if (MyApp.userPreferences.fetchPassword() != null) {
                        MyApp.userPreferences.removeRememberMe()
                    }
                }
            } else {
                //Si no estan todos los campos con datos, comprueba que cambo esta vacio y envia toast
                if (login.isEmpty() && password.isEmpty()) {
                    Toast.makeText(this,getString(R.string.emptyLoginAndPassword), Toast.LENGTH_LONG).show()
                } else if (login.isEmpty()) {
                    Toast.makeText(this,getString(R.string.emptyLogin), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,getString(R.string.emptyPassword), Toast.LENGTH_LONG).show()
                }
            }

        }

        binding.buttonChangePass.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.registerButton.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}

