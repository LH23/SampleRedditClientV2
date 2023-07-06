package io.moonlighting.redditclientv2.core.data

import io.moonlighting.redditclientv2.core.data.local.RedditPostLocal
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.remote.RedditPostRemote
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
    private val postLocalRemote1 = RedditPostLocal(postRemote1.fullname,postRemote1.title,"","","","")
    private val postLocalRemote2 = RedditPostLocal(postRemote2.fullname,postRemote2.title,"","","","")

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = RedditClientRepositoryImpl(redditPostsLocalDS, redditPostsRemoteDS)

    }

    @Test
    fun `getRedditTopPosts should fetch posts from local data source by default`() = runBlocking {
        `when`(redditPostsLocalDS.getRedditTopPosts()).thenReturn(listOf(postLocal1, postLocal2))
        `when`(redditPostsLocalDS.updateRedditLocalPosts(redditPostsRemoteDS.getRedditTopPosts())).thenAnswer {
            throw UnsupportedOperationException("updateRedditLocalPosts should not be called when updateFromRemote is false")
        }

        val posts = repository.getRedditTopPosts()
        posts.await().collect() { result ->
            assertEquals(2, result.size)
            assertEquals("10", result[0].fullname)
            assertEquals("TitleLocal1", result[0].title)
            assertEquals("11", result[1].fullname)
            assertEquals("TitleLocal2", result[1].title)
        }
    }

    @Test
    fun `getRedditTopPosts should update local data source from remote and fetch posts from local`(): Unit = runBlocking {
        `when`(redditPostsRemoteDS.getRedditTopPosts()).thenReturn(listOf(postRemote1, postRemote2))
        `when`(redditPostsLocalDS.getRedditTopPosts()).thenReturn(listOf(postLocal1, postLocal2))
        `when`(redditPostsLocalDS.updateRedditLocalPosts(redditPostsRemoteDS.getRedditTopPosts())).thenAnswer {
            `when`(redditPostsLocalDS.getRedditTopPosts()).thenReturn(listOf(postLocalRemote1, postLocalRemote2, postLocal1, postLocal2))
        }

        val posts = repository.getRedditTopPosts()
        posts.await().collect { result ->
            assertEquals(4, result.size)
            assertEquals("20", result[0].fullname)
            assertEquals("TitleRemote1", result[0].title)
            assertEquals("21", result[1].fullname)
            assertEquals("TitleRemote2", result[1].title)
            assertEquals("10", result[0].fullname)
            assertEquals("TitleLocal1", result[0].title)
            assertEquals("11", result[1].fullname)
            assertEquals("TitleLocal2", result[1].title)
        }
    }
}