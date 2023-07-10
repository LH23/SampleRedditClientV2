package io.moonlighting.redditclientv2.core.data

import io.moonlighting.redditclientv2.core.data.local.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.local.RedditPostLocal
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.remote.RedditPostRemote
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RedditClientRepositoryImplTest {

    private lateinit var repository: RedditClientRepositoryImpl

    @Mock
    private lateinit var redditPostsLocalDS: RedditPostsLocalDS

    @Mock
    private lateinit var redditPostsRemoteDS: RedditPostsRemoteDS

    private val postLocal1 = RedditPostLocal("10","TitleLocal1","","","","")
    private val postLocal2 = RedditPostLocal("11","TitleLocal2","","","","")
    private val postRemote1 = RedditPostRemote("20","TitleRemote1","","","","")
    private val postRemote2 = RedditPostRemote("21","TitleRemote2","","","","")
    private val postLocalRemote1 = RedditPostLocal(RedditPostEntity(postRemote1))
    private val postLocalRemote2 = RedditPostLocal(RedditPostEntity(postRemote2))

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = RedditClientRepositoryImpl(redditPostsLocalDS, redditPostsRemoteDS)
    }

    @Test
    fun `getRedditTopPosts should fetch posts from local data source by default`() = testScope.runTest {
        `when`(redditPostsLocalDS.getRedditTopPosts()).thenReturn(listOf(postLocal1, postLocal2))
        `when`(redditPostsLocalDS.updateRedditLocalPosts(redditPostsRemoteDS.getRedditTopPosts())).thenAnswer {
            throw UnsupportedOperationException("updateRedditLocalPosts should not be called when updateFromRemote is false")
        }

        val posts = repository.getRedditTopPosts(dispatcher=testDispatcher, updateFromRemote=false)
        posts.await().collect() { result ->
            assertEquals(2, result.size)
            assertEquals("10", result[0].fullname)
            assertEquals("TitleLocal1", result[0].title)
            assertEquals("11", result[1].fullname)
            assertEquals("TitleLocal2", result[1].title)
        }
    }

    @Test
    fun `getRedditTopPosts should replace local data source from remote`(): Unit = testScope.runTest {
        `when`(redditPostsRemoteDS.getRedditTopPosts()).thenReturn(listOf(postRemote1, postRemote2))
        `when`(redditPostsLocalDS.getRedditTopPosts()).thenReturn(listOf(postLocal1, postLocal2))
        `when`(redditPostsLocalDS.updateRedditLocalPosts(redditPostsRemoteDS.getRedditTopPosts())).thenAnswer {
            `when`(redditPostsLocalDS.getRedditTopPosts()).thenReturn(listOf(postLocalRemote1, postLocalRemote2))
        }

        val posts = repository.getRedditTopPosts(dispatcher=testDispatcher, updateFromRemote=true)
        posts.await().collect { result ->
            assertEquals(2, result.size)
            assertEquals("20", result[0].fullname)
            assertEquals("TitleRemote1", result[0].title)
            assertEquals("21", result[1].fullname)
            assertEquals("TitleRemote2", result[1].title)
        }
    }
}