package io.moonlighting.redditclientv2.core.data.local

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity

class FakeDao(
    private val fakeData: MutableList<RedditPostEntity>
): RedditPostsDao {

    override fun redditPostsDBPaging(): PagingSource<Int, RedditPostEntity> =
        fakeData.asPagingSourceFactory().invoke()

    override suspend fun removeAll() = fakeData.clear()

    override suspend fun addAll(entities: List<RedditPostEntity>) { fakeData.addAll(entities) }

    override suspend fun getCreationTime(): Long? {
        return if (fakeData.isEmpty()) null
            else fakeData.minBy { it.createdAt }.createdAt
    }

}
