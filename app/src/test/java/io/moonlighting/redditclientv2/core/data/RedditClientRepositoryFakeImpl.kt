package io.moonlighting.redditclientv2.core.data

import androidx.paging.PagingData
import io.moonlighting.redditclientv2.core.data.model.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RedditClientRepositoryFakeImpl: RedditClientRepository {

    private var posts: List<RedditPost> = listOf()
    fun addFakePosts(fakePosts: List<RedditPost>) { posts = fakePosts }

    private var errorEnabled = false
    fun setErrorOnGetPosts(error: Boolean) { errorEnabled = error }

    override fun getRedditTopPosts(subreddit: String, pageSize: Int):
            Flow<PagingData<RedditPost>> {
        if (errorEnabled) throw RuntimeException(errorMessage)
        return flowOf(PagingData.from(posts))
    }

    companion object {
        val errorMessage = "Test Exception"
        val fakePosts: List<RedditPost> = listOf(
            RedditPost("1", "Hello im a reddit post1", "r/test", "u/lio", "", ""),
            RedditPost("2", "Hello im a reddit post2", "r/test", "u/lio", "", ""),
            RedditPost("3", "Hello im a reddit post3", "r/test", "u/lio", "", ""),
            RedditPost("4", "Hello im a reddit post4", "r/test", "u/lio", "", "")
        )
    }
}