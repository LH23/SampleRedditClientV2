package io.moonlighting.redditclientv2.core.data

import androidx.annotation.VisibleForTesting
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.moonlighting.redditclientv2.core.data.local.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.paging.RedditPageMediator
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun interface RedditClientRepository {
    fun getRedditTopPosts(subreddit: String, pageSize: Int): Flow<PagingData<RedditPost>>

}


class RedditClientRepositoryImpl @Inject constructor(
    private val redditPostsLocalDS: RedditPostsLocalDS,
    private val redditPostsRemoteDS: RedditPostsRemoteDS) :
    RedditClientRepository {

    private var redditTopPosts: Flow<RepoResult<List<RedditPost>>> = flowOf()

    override fun getRedditTopPosts(subreddit: String, pageSize: Int) = getRedditTopPosts(
        subreddit, pageSize, Dispatchers.IO, true)

    @OptIn(ExperimentalPagingApi::class)
    @VisibleForTesting
    fun getRedditTopPosts(
        subreddit: String,
        pageSize: Int,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        updateFromRemote: Boolean = false
    ): Flow<PagingData<RedditPost>>{
        return Pager(
            config = PagingConfig(pageSize),
            remoteMediator = RedditPageMediator(redditPostsLocalDS, redditPostsRemoteDS, subreddit)
        ) {
            redditPostsLocalDS.getRedditTopPostsPaging(subreddit)
        }.flow.map { pagingData ->
            pagingData.map { entity -> RedditPost(entity) }
        }

//        return withContext(dispatcher) {
//            async {
//                if (updateFromRemote) {
//                    redditPostsLocalDS.updateRedditLocalPosts(redditPostsRemoteDS.getRedditTopPosts())
//                }
//                val result = redditPostsLocalDS.getRedditTopPosts().map { postLocal ->
//                    RedditPost(postLocal)
//                }
//                redditTopPosts = flowOf(
//                    RepoResult.Success(result)
//                )
//                redditTopPosts
//            }
//        }
    }
}


//class RedditClientRepositoryFakeImpl : RedditClientRepository {
//
//    override fun getRedditTopPosts(subreddit: String, pageSize: Int) =
//        withContext(Dispatchers.IO) {
//            async {
//                flowOf(RepoResult.Success(fakePosts))
//            }
//        }
//
//    companion object {
//        val fakePosts: List<RedditPost> = listOf(
//            RedditPost("1", "Hello im a reddit post1","r/test","u/lio","",""),
//            RedditPost("2", "Hello im a reddit post2","r/test","u/lio","",""),
//            RedditPost("3", "Hello im a reddit post3","r/test","u/lio","",""),
//            RedditPost("4", "Hello im a reddit post4","r/test","u/lio","","")
//        )
//    }
//}

sealed class RepoResult<out R> {
    data class Success<out T>(val posts: T) : RepoResult<T>()
    data class Error(val exception: Exception) : RepoResult<Nothing>()
    object Loading : RepoResult<Nothing>()
}


data class RedditPost(
    val fullname: String,
    val title: String,
    val subreddit: String,
    val author: String,
    val thumbnail: String,
    val url: String,
) {
    constructor(postEntity: RedditPostEntity) :
            this(
                postEntity.fullname,
                postEntity.title,
                postEntity.subreddit,
                postEntity.author,
                postEntity.thumbnail,
                postEntity.sourceUrl
            )
}
