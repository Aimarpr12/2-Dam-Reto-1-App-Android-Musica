package com.example.reto1.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthenticationResponse (
    val Login: String,
    val accessToken: String,
    val id: Integer
): Parcelable