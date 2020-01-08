package com.mvgreen.rmrtest.model.network

import com.mvgreen.rmrtest.model.network.json_objects.SearchCollectionResult
import com.mvgreen.rmrtest.model.network.json_objects.SearchPhotoResult
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    companion object {
        /** Application ID to access to the REST API. */
        const val CLIENT_ID = "a16beab0b93f63952dde7d37843827a3a2a7d17144af288e160dd36101a42398"
    }

    /**
     * Search photos and load a given [page] of the result.
     * Result is loaded by parts of the size [pageSize].
     * @param query search query.
     * @param page search result's page.
     * @param pageSize amount of elements on a single page.
     * @param clientId application id to access to the REST API, it is filled automatically.
     *
     * @return [Call] that is ready to execute.
     */
    @GET("/search/photos")
    fun findPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") pageSize: Int = 10,
        @Query("client_id") clientId: String = CLIENT_ID
    ): Call<SearchPhotoResult>

    /**
     * Search collections and load a given [page] of the result.
     * Result is loaded by parts of the size [pageSize].
     * @param query search query.
     * @param page search result's page.
     * @param pageSize amount of elements on a single page.
     * @param clientId application id to access to the REST API, it is filled automatically.
     *
     * @return [Call] that is ready to execute.
     */
    @GET("/search/collections")
    fun findCollections(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") pageSize: Int = 10,
        @Query("client_id") clientId: String = CLIENT_ID
    ): Call<SearchCollectionResult>

    /**
     * Load photos from the collection. Result is loaded by parts of the size [pageSize].
     * Parameter [page] specifies the desired page of the result.
     * @param collectionId id of the collection.
     * @param page result's page.
     * @param pageSize amount of elements on a single page.
     * @param clientId application id to access to the REST API, it is filled automatically.
     *
     * @return [Call] that is ready to execute.
     */
    @GET("/collections/{collectionId}/photos")
    fun showCollection(
        @Path("collectionId") collectionId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") pageSize: Int = 10,
        @Query("client_id") clientId: String = CLIENT_ID
    ): Call<List<UnsplashPhoto>>

    /**
     * Load a single photo by id.
     * @param photoId id of the photo.
     * @param clientId application id to access to the REST API, it is filled automatically.
     *
     * @return [Call] that is ready to execute.
     */
    @GET("/photos/{photoId}")
    fun loadPhoto(
        @Path("photoId") photoId: String,
        @Query("client_id") clientId: String = CLIENT_ID
    ) : Call<UnsplashPhoto>
}