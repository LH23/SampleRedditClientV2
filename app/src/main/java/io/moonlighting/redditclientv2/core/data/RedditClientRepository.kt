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

fun interface RedditClientRepository {
    suspend fun getRedditTopPosts(): Deferred<Flow<List<RedditPost>>>
}

class RedditClientRepositoryImpl(
    private val redditPostsLocalDS: RedditPostsLocalDS,
    private val redditPostsRemoteDS: RedditPostsRemoteDS) :
    RedditClientRepository {

    private var redditTopPosts: Flow<List<RedditPost>> = flowOf()

    override suspend fun getRedditTopPosts() = getRedditTopPosts(Dispatchers.IO, true)

    @VisibleForTesting
    private suspend fun getRedditTopPosts(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        updateFromRemote: Boolean = false
    ): Deferred<Flow<List<RedditPost>>>{
        return withContext(dispatcher) {
            async {
                if (updateFromRemote) {
                    redditPostsLocalDS.updateRedditLocalPosts(redditPostsRemoteDS.getRedditTopPosts())
                }
                redditTopPosts = flowOf(
                    redditPostsLocalDS.getRedditTopPosts().map { postLocal ->
                        RedditPost(postLocal)
                    }
                )
                redditTopPosts
            }
        }
    }
}


class RedditClientRepositoryFakeImpl : RedditClientRepository {

    override suspend fun getRedditTopPosts(): Deferred<Flow<List<RedditPost>>> =
        withContext(Dispatchers.IO) {
            async {
                flowOf(fakePosts)
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
