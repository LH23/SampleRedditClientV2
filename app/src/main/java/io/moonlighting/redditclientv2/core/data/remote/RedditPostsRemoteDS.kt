package io.moonlighting.redditclientv2.core.data.remote

import android.util.Log
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote
import javax.inject.Inject

fun interface RedditPostsRemoteDS {
    suspend fun getRedditTopPosts(
        subreddit: String,
        after: String?,
        before: String?,
        limit: Int
    ): List<RedditPostRemote>
}

class RedditPostsRemoteDSImpl @Inject constructor (
    private val apiService: RedditApiService
) : RedditPostsRemoteDS {

    private var topPostsList: List<RedditPostRemote> = listOf()

    override suspend fun getRedditTopPosts(
        subreddit: String,
        after: String?,
        before: String?,
        limit: Int
    ): List<RedditPostRemote> {
        var lastItemId = ""
        if (topPostsList.isNotEmpty()) {
            lastItemId = topPostsList[topPostsList.size - 1].fullname
        }
        val response = apiService.getTopPostsJSON(10, lastItemId)
        topPostsList = response.data.postsData.map { jsonpostdata ->
            RedditPostRemote(jsonpostdata.post)
        }
        Log.d("RedditPostsRemoteDSImpl", "Remotes ${topPostsList.map { it.title }}")
        return topPostsList
    }

}

