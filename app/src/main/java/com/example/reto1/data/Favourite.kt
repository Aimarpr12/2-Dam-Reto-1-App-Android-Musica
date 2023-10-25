package com.example.reto1.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Favourite(
    val id: Int,
    val id_user: Int,
    val id_song: Int,

    ): Parcelable