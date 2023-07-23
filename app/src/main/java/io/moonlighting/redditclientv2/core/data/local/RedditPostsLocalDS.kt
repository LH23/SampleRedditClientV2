package io.moonlighting.redditclientv2.core.data.local

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import io.moonlighting.redditclientv2.core.data.remote.RedditPostRemote
import javax.inject.Inject

interface RedditPostsLocalDS {

    @WorkerThread fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostEntity>
    @WorkerThread suspend fun updateRedditLocalPosts(posts: List<RedditPostRemote>, subreddit: String, refresh: Boolean)
    @WorkerThread suspend fun removeAllSavedPosts(subreddit: String)

}

class RedditPostsLocalDSImpl @Inject constructor(
    private val redditDAO: RedditPostsDao
) : RedditPostsLocalDS {
    companion object {
        const val TAG = "RedditPostsLocalDSImpl"
    }

    @WorkerThread
    override fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostEntity> {
        val dbpage = redditDAO.redditPostsDBPaging()
        Log.d(TAG, "dbpage: $dbpage subreddit: $subreddit")
        return dbpage
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


}