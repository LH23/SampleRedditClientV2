package io.moonlighting.redditclientv2.core.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import io.moonlighting.redditclientv2.core.data.model.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

class RedditClientRepositoryFakeImpl: RedditClientRepository {

    private val posts = mutableListOf<RedditPost>()
    private val postsFlow = MutableStateFlow(PagingData.from(posts))

    fun addFakePosts(fakePosts: List<RedditPost>) {
        posts.addAll(fakePosts)
        Log.d("FAKEREPO", "posts ${posts}")
        postsFlow.value = PagingData.from(posts)
        Log.d("FAKEREPO", "flow ${postsFlow.value}")
    }

    suspend fun emit(fakePosts: List<RedditPost>) {
        postsFlow.emit(PagingData.from(fakePosts))
    }

    private var errorEnabled = false
    fun setErrorOnGetPosts(error: Boolean) { errorEnabled = error }

    override fun getRedditTopPosts(subreddit: String, pageSize: Int): Flow<PagingData<RedditPost>> {
        if (errorEnabled) throw RuntimeException(errorMessage)
        Log.d("FAKEREPO", "flow get ${postsFlow.replayCache}")
        return postsFlow
    }


    companion object {
        const val errorMessage = "Test Exception"
        val fakePosts: List<RedditPost> = listOf(
            RedditPost("1", "Hello im a reddit post1", "r/test", "u/lio", "", ""),
            RedditPost("2", "Hello im a reddit post2", "r/test", "u/lio", "", ""),
            RedditPost("3", "Hello im a reddit post3", "r/test", "u/lio", "", ""),
            RedditPost("4", "Hello im a reddit post4", "r/test", "u/lio", "", "")
        )
    }
}