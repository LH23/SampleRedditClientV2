package io.moonlighting.redditclientv2.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class RedditPageMediator(
    private val redditPostsLocalDS: RedditPostsLocalDS,
    private val redditPostsRemoteDS: RedditPostsRemoteDS,
    private val subreddit: String
) : RemoteMediator<Int, RedditPostEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - redditPostsLocalDS.getCreationTime() < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RedditPostEntity>
    ): MediatorResult {
        try {
            val loadKey : String? = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val last = state.lastItemOrNull()
                    if (last == null) {
                        MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                    state.lastItemOrNull()?.fullname
                }
            }

            val remotePosts = redditPostsRemoteDS.getRedditTopPosts(
                subreddit = subreddit,
                after = loadKey,
                before = null,
                limit = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )
            redditPostsLocalDS.updateRedditLocalPosts(
                remotePosts, subreddit, loadType == LoadType.REFRESH
            )

            return MediatorResult.Success(endOfPaginationReached = remotePosts.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

}
