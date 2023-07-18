package io.moonlighting.redditclientv2.core.data.local

import android.util.Log
import androidx.paging.PagingSource
import io.moonlighting.redditclientv2.core.data.RedditPost
import io.moonlighting.redditclientv2.core.data.remote.RedditPostRemote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RedditPostsLocalDS {

    fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostEntity>
    fun updateRedditLocalPosts(posts: List<RedditPostRemote>, subreddit: String)
    fun removeAllSavedPosts(subreddit: String)

}

class RedditPostsLocalDSImpl @Inject constructor(
    private val redditDAO: RedditPostsDao
) : RedditPostsLocalDS {
    companion object {
        const val TAG = "RedditPostsLocalDSImpl"
    }

    //    override fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostLocal> {
//        val topPostsList = redditDAO.redditPostsDBPaging(subreddit).flow.map { postDB -> RedditPostLocal(postDB) }
//        Log.d(TAG, "Local ${topPostsList.map { it.title }}")
//        return topPostsList
//    }

    override fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostEntity> {
        return redditDAO.redditPostsDBPaging(subreddit)
    }

    override fun updateRedditLocalPosts(posts: List<RedditPostRemote>, subreddit: String) {
        // TODO something smarter here, need to compare the remote with the current local
        removeAllSavedPosts(subreddit)
        Log.d(TAG, "Adding ${posts.map { it.title }}")
        redditDAO.addAll(posts.map { remote -> RedditPostEntity(remote) })
    }

    override fun removeAllSavedPosts(subreddit: String): Unit = redditDAO.removeAll(subreddit)


}
//
//data class RedditPostLocal(val fullname: String,
//                            val title: String,
//                            val subreddit: String,
//                            val author: String,
//                            val thumbnail: String,
//                            val url: String,
//) {
//    constructor(dbPost: RedditPostEntity) :
//            this(
//                dbPost.fullname,
//                dbPost.title,
//                dbPost.subreddit,
//                dbPost.author,
//                dbPost.thumbnail,
//                dbPost.sourceUrl
//            )
//}