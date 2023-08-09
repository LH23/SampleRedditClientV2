package io.moonlighting.redditclientv2.core.data.local

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote

class FakeRedditPostsLocalDS(
    private val fakeData: MutableList<RedditPostEntity>
) : RedditPostsLocalDS {

    override fun getRedditTopPostsPaging(subreddit: String): PagingSource<Int, RedditPostEntity> =
        fakeData.asPagingSourceFactory().invoke()

    override suspend fun updateRedditLocalPosts(
        posts: List<RedditPostRemote>,
        subreddit: String,
        refresh: Boolean
    ) {
        if (refresh) {
            fakeData.clear()
            fakeData.addAll(posts.map { RedditPostEntity(it) })
        }
    }

    override suspend fun removeAllSavedPosts(subreddit: String) =
        fakeData.clear()

    override suspend fun getCreationTime(): Long =
        fakeData.minBy { it.createdAt }.createdAt

}
