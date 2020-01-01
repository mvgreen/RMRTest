package com.mvgreen.rmrtest.network.json_objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashUrls(
    val thumb: String?,
    val small: String,
    val medium: String?,
    val regular: String?,
    val large: String?,
    val full: String?,
    val raw: String?
) : Parcelable
