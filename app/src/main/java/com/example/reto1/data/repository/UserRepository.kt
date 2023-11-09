package com.example.reto1.data.repository

import com.example.reto1.data.CommonUserRepository
import com.example.reto1.data.RegistrationCheck
import com.example.reto1.data.User

class UserRepository(private val repository : CommonUserRepository): CommonUserRepository {

    override suspend fun getUser() = repository.getUser()
    override suspend fun loginUser(user: User) = repository.loginUser(user)

    override suspend fun registerUser(user: User) = repository.registerUser(user)

    override suspend fun changePassword(user: User) = repository.changePassword(user)

    override suspend fun checkEmail(registrationCheck: RegistrationCheck) = repository.checkEmail(registrationCheck)

    override suspend fun checkLogin(registrationCheck: RegistrationCheck) = repository.checkLogin(registrationCheck)


}