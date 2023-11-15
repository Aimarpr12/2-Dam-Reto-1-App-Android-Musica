package com.example.reto1.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    val id: Int?,
    val name: String?,
    val surname: String?,
    val login: String?,
    val email: String?,
    val password: String,
    val oldPassword: String?
): Parcelable