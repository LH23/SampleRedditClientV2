package io.moonlighting.redditclientv2.core.data

import androidx.annotation.VisibleForTesting
import io.moonlighting.redditclientv2.core.data.local.RedditPostLocal
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

fun interface RedditClientRepository {
    suspend fun getRedditTopPosts(): Deferred<Flow<RepoResult<List<RedditPost>>>>
}


class RedditClientRepositoryImpl @Inject constructor(
    private val redditPostsLocalDS: RedditPostsLocalDS,
    private val redditPostsRemoteDS: RedditPostsRemoteDS) :
    RedditClientRepository {

    private var redditTopPosts: Flow<RepoResult<List<RedditPost>>> = flowOf()

    override suspend fun getRedditTopPosts() = getRedditTopPosts(Dispatchers.IO, true)

    @VisibleForTesting
    suspend fun getRedditTopPosts(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        updateFromRemote: Boolean = false
    ): Deferred<Flow<RepoResult<List<RedditPost>>>>{
        return withContext(dispatcher) {
            async {
                if (updateFromRemote) {
                    redditPostsLocalDS.updateRedditLocalPosts(redditPostsRemoteDS.getRedditTopPosts())
                }
                val result = redditPostsLocalDS.getRedditTopPosts().map { postLocal ->
                    RedditPost(postLocal)
                }
                redditTopPosts = flowOf(
                    RepoResult.Success(result)
                )
                redditTopPosts
            }
        }
    }
}


class RedditClientRepositoryFakeImpl : RedditClientRepository {

    override suspend fun getRedditTopPosts(): Deferred<Flow<RepoResult<List<RedditPost>>>> =
        withContext(Dispatchers.IO) {
            async {
                flowOf(RepoResult.Success(fakePosts))
            }
        }

    companion object {
        val fakePosts: List<RedditPost> = listOf(
            RedditPost("1", "Hello im a reddit post1","r/test","u/lio","",""),
            RedditPost("2", "Hello im a reddit post2","r/test","u/lio","",""),
            RedditPost("3", "Hello im a reddit post3","r/test","u/lio","",""),
            RedditPost("4", "Hello im a reddit post4","r/test","u/lio","","")
        )
    }

}

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
    constructor(postLocal: RedditPostLocal) :
            this(
                postLocal.fullname,
                postLocal.title,
                postLocal.subreddit,
                postLocal.author,
                postLocal.thumbnail,
                postLocal.url
            )
}
