package com.example.reto1.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Song(
    val id: Int,
    val url: String,
    val title: String,
    val author: String,
    val favorite: Boolean,
    var selected: Boolean = false
): Parcelable {
        constructor(url: String, title: String, author: String, favorite: Boolean ) : this(0, url, title, author, favorite) {
        }
        constructor() : this(0, "", "", "", false) {
        }
    }
