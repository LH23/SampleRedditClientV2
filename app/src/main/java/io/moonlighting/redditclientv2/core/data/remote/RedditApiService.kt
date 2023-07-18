package io.moonlighting.redditclientv2.core.data.remote

import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

fun interface RedditApiService {
    @GET("/top.json")
    suspend fun getTopPostsJSON(
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): RedditPostsJSONResponse
}

data class RedditPostsJSONResponse(@Json(name = "data") val data: PostsJsonData) {
    class PostsJsonData(@Json(name = "children") val postsData: List<JsonPostData>) {
        class JsonPostData(
            @Json(name = "kind") val kind: String,
            @Json(name = "data") val post: JsonPost
        ) {
            class JsonPost(
                @Json(name = "name") val name: String,
                @Json(name = "subreddit_name_prefixed") val subredditNamePrefixed: String,
                @Json(name = "title") val title: String,
                @Json(name = "author") val author: String,
                @Json(name = "created_utc") val createdUtc: Long,
                @Json(name = "thumbnail") val thumbnail: String,
                @Json(name = "num_comments") val numComments: Int,
                @Json(name = "url") val url: String,
                @Json(name = "preview") val preview: Preview?
            ) {
                class Preview(@Json(name = "images") val images: List<Image>) {
                    class Image(@Json(name = "source") val source: Source) {
                        class Source(@Json(name = "url") val url: String)
                    }
                }
            }
        }
    }
}

