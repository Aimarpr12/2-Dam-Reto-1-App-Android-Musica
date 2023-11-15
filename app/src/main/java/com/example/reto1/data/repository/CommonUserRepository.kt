package com.example.reto1.data

import com.example.reto1.data.repository.remote.RetrofitClient
import com.example.reto1.utils.Resource

interface CommonUserRepository {

    suspend fun loginUser(user: User): Resource<AuthenticationResponse>

    suspend fun registerUser(user: User): Resource<Void>

    suspend fun changePassword(user: User): Resource<Void>

    suspend fun checkEmail(registrationCheck: RegistrationCheck): Resource<Void>

    suspend fun checkLogin(registrationCheck: RegistrationCheck): Resource<Void>
}