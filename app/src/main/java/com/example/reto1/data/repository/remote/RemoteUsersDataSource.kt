package com.example.reto1.data.repository.remote

import com.example.reto1.data.AuthenticationResponse
import com.example.reto1.utils.Resource
import com.example.reto1.data.User
import com.example.reto1.data.CommonUserRepository

class RemoteUsersDataSource: BaseDataSource(), CommonUserRepository {

    override suspend fun getUser() = getResult {
        RetrofitClient.apiInterface.getUser()
    }

    override suspend fun loginUser(user: User) = getResult {
        RetrofitClient.apiInterface.loginUser(user)
    }

    override suspend fun registerUser(user: User) = getResult {
        RetrofitClient.apiInterface.registerUser(user)
    }

    override suspend fun changePassword(user: User) = getResult {
        RetrofitClient.apiInterface.changePassword(user)
    }

}