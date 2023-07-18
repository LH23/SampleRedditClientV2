package io.moonlighting.redditclientv2.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.moonlighting.redditclientv2.core.data.local.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS

@OptIn(ExperimentalPagingApi::class)
class RedditPageMediator(
    redditPostsLocalDS: RedditPostsLocalDS,
    redditPostsRemoteDS: RedditPostsRemoteDS,
    subreddit: String
) : RemoteMediator<Int, RedditPostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RedditPostEntity>
    ): MediatorResult {
        TODO("Not yet implemented")
    }

}
