package com.mvgreen.rmrtest.model.network.json_objects

data class SearchPhotoResult(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashPhoto>
)
