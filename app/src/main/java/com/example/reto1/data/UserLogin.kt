package com.example.reto1.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserLogin(
    val login: String,
    val password: String
): Parcelable