package com.mvgreen.rmrtest.model.network.json_objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashCollection(
    val id: Int,
    val title: String,
    val description: String?,
    val published_at: String,
    val featured: Boolean,
    val total_photos: Int,
    val private: Boolean,
    val share_key: String,
    val cover_photo: UnsplashPhoto,
    val user: UnsplashUser,
    val links: UnsplashLinks
) : Parcelable, ResultListItem
