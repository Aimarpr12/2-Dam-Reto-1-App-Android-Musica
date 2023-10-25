package com.example.reto1.data.repository.remote

import android.util.Log
import retrofit2.Response
import com.example.reto1.utils.Resource

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.success(body)

                }else{
                    return Resource.success()
                }
            }
            // podria no ser un error ...
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e("PruebaDia1", message)
        return Resource.error("Network call has failed for a following reason: $message")
    }
}