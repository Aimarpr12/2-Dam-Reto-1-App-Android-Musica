package com.example.reto1.ui.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reto1.data.AuthenticationResponse
import com.example.reto1.data.CommonUserRepository
import com.example.reto1.data.RegistrationCheck
import com.example.reto1.data.User
import com.example.reto1.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: CommonUserRepository
) : ViewModel() {

    private val _login = MutableLiveData<Resource<AuthenticationResponse>>();
    val login : LiveData<Resource<AuthenticationResponse>> get() = _login

    private val _register = MutableLiveData<Resource<Void>>();
    val register : LiveData<Resource<Void>> get() = _register

    private val _changePassword = MutableLiveData<Resource<Void>>();
    val changePassword : LiveData<Resource<Void>> get() = _changePassword

    private var _checkEmail = MutableLiveData<Resource<Void>>();
    val checkEmail : LiveData<Resource<Void>> get() = _checkEmail

    private var _checkLogin = MutableLiveData<Resource<Void>>();
    val checkLogin : LiveData<Resource<Void>> get() = _checkLogin

//    private val _user = MutableLiveData<Resource<GetUserResponse>>();
//    val user : LiveData<Resource<GetUserResponse>> get() = _user


    fun loginUser(login: String, password: String) {
        viewModelScope.launch {
            val user = User(null,null,null,login,null,password,null)
            _login.value = logUserInRepository(user)
        }
    }

    private suspend fun logUserInRepository(user: User) : Resource<AuthenticationResponse> {
        return withContext(Dispatchers.IO) {
            userRepository.loginUser(user)
        }
    }

    fun checkEmail(email: String) {
        viewModelScope.launch {
            val check = RegistrationCheck(null,email)
            _checkEmail.value = checkEmailInRepository(check)
        }
    }

    private suspend fun checkEmailInRepository(check: RegistrationCheck) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.checkEmail(check)
        }
    }

    fun checkLogin(login: String) {
        viewModelScope.launch {
            val check = RegistrationCheck(login,null)
            _checkLogin.value = checkLoginInRepository(check)
        }
    }

    private suspend fun checkLoginInRepository(check: RegistrationCheck) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.checkLogin(check)
        }
    }


    fun changePassword(login: String,newPassword: String, currentPassword: String) {
        viewModelScope.launch {
            val user = User(null,null,null,login,null,newPassword,currentPassword)
            Log.e("loginUser - Login",user.login)
            Log.e("loginUser - Password",user.password)
            user.oldPassword?.let { Log.e("changePassword - oldPassword", it) }
            _changePassword.value = changePasswordInRepository(user)
        }

    }

    private suspend fun changePasswordInRepository(user: User) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.changePassword(user)
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
