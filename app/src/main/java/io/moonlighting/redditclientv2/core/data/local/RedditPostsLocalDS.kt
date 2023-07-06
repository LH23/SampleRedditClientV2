package io.moonlighting.redditclientv2.core.data.local

import android.util.Log
import io.moonlighting.redditclientv2.core.data.remote.RedditPostRemote

interface RedditPostsLocalDS {
    fun getRedditTopPosts(): List<RedditPostLocal>
    fun updateRedditLocalPosts(posts: List<RedditPostRemote>)
    fun removeAllSavedPosts()
}

class RedditPostsLocalDSImpl(private val redditDAO: RedditPostsDao) : RedditPostsLocalDS {
    companion object {
        const val TAG = "RedditPostsLocalDSImpl"
    }

    override fun getRedditTopPosts(): List<RedditPostLocal> {
        val topPostsList = redditDAO.redditPostsDB.map { postDB -> RedditPostLocal(postDB) }
        Log.d(TAG, "Local ${topPostsList.map { it.title }}")
        return topPostsList
    }

    override fun updateRedditLocalPosts(posts: List<RedditPostRemote>) {
        // TODO something smarter here, need to compare the remote with the current local
        removeAllSavedPosts()
        Log.d(TAG, "Adding ${posts.map { it.title }}")
        redditDAO.addAll(posts.map { remote -> RedditPostEntity(remote) })
    }

    override fun removeAllSavedPosts(): Unit = redditDAO.removeAll()



}

data class RedditPostLocal(val fullname: String,
                            val title: String,
                            val subreddit: String,
                            val author: String,
                            val thumbnail: String,
                            val url: String,
) {
    constructor(dbPost: RedditPostEntity) :
            this(
                dbPost.fullname,
                dbPost.title,
                dbPost.subreddit,
                dbPost.author,
                dbPost.thumbnail,
                dbPost.sourceUrl
            )
}