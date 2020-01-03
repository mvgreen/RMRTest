package com.mvgreen.rmrtest.model.network.json_objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchCollectionResult(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashCollection>?
) : Parcelable