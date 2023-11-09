package com.example.reto1.ui.users

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.reto1.R
import com.example.reto1.data.repository.remote.RemoteUsersDataSource
import com.example.reto1.databinding.LayoutRegisterBinding
import com.example.reto1.utils.Resource


class RegisterActivity: ComponentActivity() {

    private val userRepository = RemoteUsersDataSource()

    private val viewModel: UserViewModel by viewModels { UsersViewModelFactory(
        userRepository
    ) }
    @SuppressLint("ResourceAsColor")
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
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("login",binding.registerLogin.text.toString())
                    intent.putExtra("password",binding.registerPassword1.text.toString())
                    startActivity(intent)
                    finish()
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

        viewModel.checkEmail.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.i("CheckEmail", "Ha detectado cambio - Observer")
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    Log.i("Gorka_UserActivity", "Email in Repository - Success")
                    binding.emailWrong.visibility = View.INVISIBLE
                    binding.emailWrong.text = ""
                    binding.registerButton.isEnabled = true;
                }

                Resource.Status.ERROR -> {
                    Log.i("Gorka_UserActivity", "Email in Repository - Toast")
                    binding.emailWrong.visibility = View.VISIBLE
                    binding.emailWrong.text = getString(R.string.emailAlreadyInRepository)
                    binding.emailWrong.setTextColor(R.color.Red)
                    binding.registerButton.isEnabled = false;
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })

        viewModel.checkLogin.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.i("CheckEmail", "Ha detectado cambio - Observer")
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    Log.i("Gorka_UserActivity", "Email in Repository - Success")
                    binding.LoginWrong.visibility = View.INVISIBLE
                    binding.LoginWrong.text = ""
                    binding.registerButton.isEnabled = true;
                }

                Resource.Status.ERROR -> {
                    Log.i("Gorka_UserActivity", "Email in Repository - Toast")
                    binding.LoginWrong.visibility = View.VISIBLE
                    binding.LoginWrong.text = getString(R.string.emailAlreadyInRepository)
                    binding.LoginWrong.setTextColor(R.color.Red)
                    binding.registerButton.isEnabled = false;
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })


        binding.registerEmail.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.registerEmail.text.toString()).matches()) {
                    viewModel.checkEmail(binding.registerEmail.text.toString())
                    Log.i("Gorka_UserActivity", "ChangedEmail - Correct")
                } else {
                    Log.i("Gorka_UserActivity", "ChangedEmail - NotEmail")
                }
            }
        }

        binding.registerLogin.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                viewModel.checkLogin(binding.registerLogin.text.toString().lowercase())
            }
        }


        binding.registerButton.setOnClickListener() {
            val password1 = binding.registerPassword1.text.toString();
            val password2 = binding.registerPassword2.text.toString()
            if (password1 == password2) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.registerEmail.text.toString()).matches())
                {
                    viewModel.registerUser(
                        binding.registerName.text.toString(),
                        binding.registerSurname.text.toString(),
                        binding.registerEmail.text.toString(),
                        binding.registerLogin.text.toString().lowercase(),
                        binding.registerPassword1.text.toString()
                    )
                } else {
                    Toast.makeText(this,getString(R.string.wrongEmail),Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(this,getString(R.string.noequalpassword), Toast.LENGTH_LONG).show()
            }

        }

        binding.backButton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}

