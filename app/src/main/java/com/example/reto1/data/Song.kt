package com.example.reto1.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Song(
    val url: String,
    val title: String,
    val author: String
): Parcelable