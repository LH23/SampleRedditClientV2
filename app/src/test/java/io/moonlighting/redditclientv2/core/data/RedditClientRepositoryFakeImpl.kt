package io.moonlighting.redditclientv2.core.data

import android.util.Log
import android.widget.DatePicker
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.testing.TestPager
import androidx.paging.testing.asPagingSourceFactory
import io.moonlighting.redditclientv2.core.data.model.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.util.Date

class RedditClientRepositoryFakeImpl: RedditClientRepository {

    private val posts = mutableListOf<RedditPost>()

    fun addFakePosts(fakePosts: List<RedditPost>) {
        posts.addAll(fakePosts)
        Log.d("FAKEREPO", "posts ${posts}")
    }


    private var errorEnabled = false
    fun setErrorOnGetPosts(error: Boolean) { errorEnabled = error }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRedditTopPosts(subreddit: String, pageSize: Int): Flow<PagingData<RedditPost>> {
        if (errorEnabled) throw RuntimeException(errorMessage)
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                posts.asPagingSourceFactory().invoke()
            }
        ).flow
    }

    override fun getRedditPost(fullname: String): Flow<RedditPost> {
        TODO("Not yet implemented")
    }

    companion object {
        const val errorMessage = "Test Exception"
        val fakePosts: List<RedditPost> = listOf(
            RedditPost("1", "Hello im a reddit post1", "r/test", "u/lio", "", "", "", "", "", "", Date.from(Instant.now())),
            RedditPost("2", "Hello im a reddit post2", "r/test", "u/lio", "", "", "", "", "", "", Date.from(Instant.now())),
            RedditPost("3", "Hello im a reddit post3", "r/test", "u/lio", "", "", "", "", "", "", Date.from(Instant.now())),
            RedditPost("4", "Hello im a reddit post4", "r/test", "u/lio", "", "", "", "", "", "", Date.from(Instant.now()))
        )
    }
}