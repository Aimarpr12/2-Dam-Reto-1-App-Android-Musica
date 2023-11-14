package com.example.reto1.ui.users

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.reto1.MyApp
import com.example.reto1.R
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
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    Toast.makeText(this,getString(R.string.changedPassword), Toast.LENGTH_LONG).show()
                    if (MyApp.userPreferences.fetchPassword() != null) {
                        MyApp.userPreferences.saveRememberMe(binding.newPassword.text.toString())
                    }
                    finish()
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this,getString(R.string.notChangedPassword), Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    //DE MOMENTO NADA
                }
            }
        })

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.changePasswordButton.setOnClickListener() {
            if (binding.password.text.isNotEmpty() && binding.newPassword.text.isNotEmpty() && binding.newPassword2.text.isNotEmpty()) {
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
                } else {
                    Toast.makeText(this,getString(R.string.noequalpassword), Toast.LENGTH_LONG).show()
                    eraseChangePassword(binding)
                }
            } else {
                if (binding.password.text.isNullOrEmpty()) {
                    Toast.makeText(this,getString(R.string.emptyCurrentPassword), Toast.LENGTH_LONG).show()
                }
                if (binding.newPassword.text.isNullOrEmpty() || binding.newPassword2.text.isNullOrEmpty()) {
                    Toast.makeText(this,getString(R.string.emptyNewPassword), Toast.LENGTH_LONG).show()
                }
                eraseChangePassword(binding)
            }

        }

    }

    private fun eraseChangePassword(binding: LayoutChangePasswordBinding) {
        binding.password.setText("")
        binding.newPassword.setText("")
        binding.newPassword2.setText("")
    }
}