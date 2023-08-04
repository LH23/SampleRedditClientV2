package io.moonlighting.redditclientv2.core.data.local

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote
import javax.inject.Inject

interface RedditPostsLocalDS {

    @WorkerThread fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostEntity>
    @WorkerThread suspend fun updateRedditLocalPosts(posts: List<RedditPostRemote>, subreddit: String, refresh: Boolean)
    @WorkerThread suspend fun removeAllSavedPosts(subreddit: String)
    @WorkerThread suspend fun getCreationTime(): Long

}

class RedditPostsLocalDSImpl @Inject constructor(
    private val redditDAO: RedditPostsDao
) : RedditPostsLocalDS {
    companion object {
        const val TAG = "RedditPostsLocalDSImpl"
    }

    @WorkerThread
    override fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostEntity> {
        return redditDAO.redditPostsDBPaging()
    }

    @WorkerThread
    override suspend fun updateRedditLocalPosts(posts: List<RedditPostRemote>, subreddit: String, refresh: Boolean) {
        // TODO something smarter here, need to compare the remote with the current local
        if (refresh) removeAllSavedPosts(subreddit)
        Log.d(TAG, "Adding ${posts.map { it.title }}")
        redditDAO.addAll(posts.map { remote -> RedditPostEntity(remote) })
    }

    @WorkerThread
    override suspend fun removeAllSavedPosts(subreddit: String): Unit = redditDAO.removeAll()

    @WorkerThread
    override suspend fun getCreationTime() = redditDAO.getCreationTime()?: 0


}