package io.moonlighting.redditclientv2.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

fun interface RedditApiService {
    @GET("/top.json")
    suspend fun getTopPostsJSON(
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): RedditPostsJSONResponse
}

