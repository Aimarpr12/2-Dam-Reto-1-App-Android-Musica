package com.example.reto1.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RegistrationCheck(
    val login: String?,
    val email: String?
): Parcelable