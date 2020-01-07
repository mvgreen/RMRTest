package com.mvgreen.rmrtest.model.network

import com.mvgreen.rmrtest.model.network.json_objects.SearchCollectionResult
import com.mvgreen.rmrtest.model.network.json_objects.SearchPhotoResult
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {
    @GET("/search/photos")
    fun findPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") pageSize: Int = 10,
        @Query("client_id") clientId: String = CLIENT_ID
    ): Call<SearchPhotoResult>

    @GET("/search/collections")
    fun findCollections(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") pageSize: Int = 10,
        @Query("client_id") clientId: String = CLIENT_ID
    ): Call<SearchCollectionResult>

    @GET("/collections/{collectionId}/photos")
    fun showCollection(
        @Path("collectionId") collectionId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") pageSize: Int = 10,
        @Query("client_id") clientId: String = CLIENT_ID
    ): Call<List<UnsplashPhoto>>

    @GET("/photos/{photoId}")
    fun loadPhotoOfTheDay(
        @Path("photoId") photoId: String,
        @Query("client_id") clientId: String = CLIENT_ID
    ) : Call<UnsplashPhoto>

    companion object {
        const val CLIENT_ID = "a16beab0b93f63952dde7d37843827a3a2a7d17144af288e160dd36101a42398"
    }
}