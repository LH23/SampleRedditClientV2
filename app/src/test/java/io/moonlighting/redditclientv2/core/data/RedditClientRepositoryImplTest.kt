package io.moonlighting.redditclientv2.core.data

import androidx.paging.testing.asSnapshot
import io.moonlighting.redditclientv2.core.data.local.FakeRedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.model.RedditPost
import io.moonlighting.redditclientv2.core.data.remote.FakeRedditPostsRemoteDS
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RedditClientRepositoryImplTest {

    private lateinit var repository: RedditClientRepositoryImpl

    private val postLocal1 = RedditPostEntity(null, "10","TitleLocal1","","","","")
    private val postLocal2 = RedditPostEntity(null, "11","TitleLocal2","","","","")
    private val postRemote1 = RedditPostRemote("20","TitleRemote1","","","","")
    private val postRemote2 = RedditPostRemote("21","TitleRemote2","","","","")
    private val postLocalRemote1 = RedditPostEntity(postRemote1)
    private val postLocalRemote2 = RedditPostEntity(postRemote2)

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val fakeLocalDS = FakeRedditPostsLocalDS(mutableListOf(postLocal1, postLocal2))
        val fakeRemoteDS = FakeRedditPostsRemoteDS(listOf(postRemote1, postRemote2))
        repository = RedditClientRepositoryImpl(fakeLocalDS, fakeRemoteDS)
    }

    @Test
    fun `getRedditTopPosts should fetch posts from local data source by default`() = testScope.runTest {

        // refresh false
        val posts = repository.getRedditTopPosts("test",10, refresh = false)

        val resultPosts: List<RedditPost> = posts.asSnapshot {
            scrollTo(index = 3)
        }

        assertEquals(listOf(postLocal1, postLocal2).map { RedditPost(it) }, resultPosts)
    }

    @Test
    fun `getRedditTopPosts should replace local data source from remote`(): Unit = testScope.runTest {
        // TODO test failing, because getRedditTopPosts does not refresh local list as required
        // refresh true
        val posts = repository.getRedditTopPosts("test",10, refresh = true)

        val resultPosts: List<RedditPost> = posts.asSnapshot {
            scrollTo(index = 3)
        }

        assertEquals(listOf(postLocalRemote1, postLocalRemote2).map { RedditPost(it) }, resultPosts)

    }
}