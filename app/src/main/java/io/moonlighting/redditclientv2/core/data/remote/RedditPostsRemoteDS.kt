package io.moonlighting.redditclientv2.core.data.remote

import android.util.Log

fun interface RedditPostsRemoteDS {
    fun getRedditTopPosts(): List<RedditPostRemote>
}

class RedditPostsRemoteDSImpl(private val apiService: RedditApiService) : RedditPostsRemoteDS {

    private var topPostsList: List<RedditPostRemote>? = listOf()

    override fun getRedditTopPosts(): List<RedditPostRemote> {
        var lastItemId = ""
        if (topPostsList != null && topPostsList!!.isNotEmpty()) {
            lastItemId = topPostsList!![topPostsList!!.size - 1].fullname
        }
        val response = apiService.getTopPostsJSON(10, lastItemId).execute()
        if (response.isSuccessful) {
            topPostsList = response.body()?.data?.postsData?.map { jsonpostdata ->
                RedditPostRemote(jsonpostdata.post)
            }
        }
        Log.d("RedditPostsRemoteDSImpl", "Remotes ${topPostsList?.map { it.title }}")
        return topPostsList!!
    }

}

data class RedditPostRemote(val fullname: String,
                            val title: String,
                            val subreddit: String,
                            val author: String,
                            val thumbnail: String,
                            val url: String,
) {
    constructor(jsonPost: RedditPostsJSONResponse.PostsJsonData.JsonPostData.JsonPost) :
            this(jsonPost.name,
                jsonPost.title,
                jsonPost.subredditNamePrefixed,
                jsonPost.author,
                jsonPost.thumbnail,
                jsonPost.url
            )
}