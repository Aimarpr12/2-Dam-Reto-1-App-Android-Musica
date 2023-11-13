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
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("login",binding.registerLogin.text.toString())
                    intent.putExtra("password",binding.registerPassword1.text.toString())
                    startActivity(intent)
                    finish()
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this,getString(R.string.regiterInfoLeft), Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })

        viewModel.checkEmail.observe(this, Observer {
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    binding.emailWrong.visibility = View.INVISIBLE
                    binding.emailWrong.text = ""
                    binding.registerButton.isEnabled = true;
                }

                Resource.Status.ERROR -> {
                    binding.emailWrong.visibility = View.VISIBLE
                    binding.emailWrong.text = getString(R.string.emailAlreadyInRepository)
                    binding.registerButton.isEnabled = false;
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })

        viewModel.checkLogin.observe(this, Observer {
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    binding.LoginWrong.visibility = View.INVISIBLE
                    binding.LoginWrong.text = ""
                    binding.registerButton.isEnabled = true;
                }

                Resource.Status.ERROR -> {
                    binding.LoginWrong.visibility = View.VISIBLE
                    binding.LoginWrong.text = getString(R.string.loginAlreadyInRepository)
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
                } else {
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

