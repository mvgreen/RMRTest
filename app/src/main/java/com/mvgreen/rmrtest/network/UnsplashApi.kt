package com.mvgreen.rmrtest.network

import com.mvgreen.rmrtest.network.json_objects.SearchCollectionResult
import com.mvgreen.rmrtest.network.json_objects.SearchPhotoResult
import com.mvgreen.rmrtest.network.json_objects.UnsplashPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface   UnsplashApi {
    @GET("/search/photos")
    fun searchPhotos(
        @Query("client_id") clientId: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Call<SearchPhotoResult>

    @GET("/search/collections")
    fun searchCollections(
        @Query("client_id") clientId: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Call<SearchCollectionResult>

    @GET("/collections/{collectionId}/photos")
    fun showCollection(
        @Path("collectionId") collectionId: Int,
        @Query("client_id") clientId: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Call<List<UnsplashPhoto>>

    companion object {
        const val CLIENT_ID = "a16beab0b93f63952dde7d37843827a3a2a7d17144af288e160dd36101a42398"
    }
}