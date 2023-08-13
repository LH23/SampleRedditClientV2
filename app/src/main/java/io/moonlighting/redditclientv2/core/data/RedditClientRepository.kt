package io.moonlighting.redditclientv2.core.data

import androidx.annotation.VisibleForTesting
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.model.RedditPost
import io.moonlighting.redditclientv2.core.data.paging.RedditPageMediator
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun interface RedditClientRepository {
    fun getRedditTopPosts(subreddit: String, pageSize: Int): Flow<PagingData<RedditPost>>

}


class RedditClientRepositoryImpl @Inject constructor(
    private val redditPostsLocalDS: RedditPostsLocalDS,
    private val redditPostsRemoteDS: RedditPostsRemoteDS
): RedditClientRepository {

    override fun getRedditTopPosts(subreddit: String, pageSize: Int) =
        getRedditTopPosts(subreddit, pageSize, false)

    @OptIn(ExperimentalPagingApi::class)
    @VisibleForTesting
    fun getRedditTopPosts(
        subreddit: String,
        pageSize: Int,
        refresh: Boolean = false
    ): Flow<PagingData<RedditPost>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true,
                initialLoadSize = pageSize*2
            ),
            remoteMediator = RedditPageMediator(redditPostsLocalDS, redditPostsRemoteDS, subreddit, refresh),
            pagingSourceFactory = { redditPostsLocalDS.getRedditTopPostsPaging(subreddit) }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                println("entity ${entity.gid} ${entity.title}")
                RedditPost(entity)
            }
        }
    }
}

