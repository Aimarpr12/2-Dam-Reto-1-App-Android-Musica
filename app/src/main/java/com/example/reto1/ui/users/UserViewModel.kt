package com.example.reto1.ui.users

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reto1.data.CommonUserRepository
import com.example.reto1.data.User
import com.example.reto1.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: CommonUserRepository
) : ViewModel() {

    private val _login = MutableLiveData<Resource<Void>>();
    val login : LiveData<Resource<Void>> get() = _login

    private val _register = MutableLiveData<Resource<Void>>();
    val register : LiveData<Resource<Void>> get() = _register

    private val _changePassword = MutableLiveData<Resource<Void>>();
    val changePassword : LiveData<Resource<Void>> get() = _changePassword


    fun loginUser(login: String, password: String) {
        viewModelScope.launch {
            val user = User(null,null,null,login,null,password,null)
            _login.value = logUserInRepository(user)
        }
    }

    private suspend fun logUserInRepository(user: User) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.loginUser(user)
        }
    }

    fun changePassword(login: String,currentPassword: String, newPassword1: String) {
        viewModelScope.launch {
            val user = User(null,null,null,login,null,newPassword1,currentPassword)
            Log.e("loginUser - Login",user.login)
            Log.e("loginUser - Password",user.password)
            _changePassword.value = changePasswordInRepository(user)
        }

    }

    private suspend fun changePasswordInRepository(user: User) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.loginUser(user)
        }
    }

    fun registerUser(name: String, surname: String, email: String, login: String, password: String) {
        viewModelScope.launch {
            val user = User(null,name,surname,login,email,password,null)
            _register.value  = registerUserInRepository(user)
        }
    }

    private suspend fun registerUserInRepository(user: User) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.registerUser(user)
        }
    }
}

class UsersViewModelFactory(
    private val userRepository: CommonUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository) as T
    }
}
