package io.moonlighting.redditclientv2.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.model.RedditPost
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        repository = RedditClientRepositoryImpl(redditPostsLocalDS, redditPostsRemoteDS)
    }

    @Test
    fun `getRedditTopPosts should fetch posts from local data source by default`() = testScope.runTest {
        `when`(redditPostsLocalDS.getRedditTopPostsPaging("")).thenReturn(listOf(postLocal1, postLocal2).toPagingSource())
        `when`(redditPostsLocalDS.updateRedditLocalPosts(
            redditPostsRemoteDS.getRedditTopPosts("",null,null,10), "", false
        )
        ).thenAnswer {
            throw UnsupportedOperationException("updateRedditLocalPosts should not be called when updateFromRemote is false")
        }

        val posts = repository.getRedditTopPosts("test",10, refresh = false)
        posts.collect() {
            // TODO get it.posts from pagingData to list using TestPager
            val resultPosts: List<RedditPost> = null!! // it.extractElementsFromPagingData

            assertEquals(2, resultPosts.size)
            assertEquals("10", resultPosts[0].fullname)
            assertEquals("TitleLocal1", resultPosts[0].title)
            assertEquals("11", resultPosts[1].fullname)
            assertEquals("TitleLocal2", resultPosts[1].title)
        }
    }

    @Test
    fun `getRedditTopPosts should replace local data source from remote`(): Unit = testScope.runTest {
        `when`(redditPostsRemoteDS.getRedditTopPosts("",null,null,10)).thenReturn(listOf(postRemote1, postRemote2))
        `when`(redditPostsLocalDS.getRedditTopPostsPaging("")).thenReturn(listOf(postLocal1, postLocal2).toPagingSource())
        `when`(redditPostsLocalDS.updateRedditLocalPosts(
            redditPostsRemoteDS.getRedditTopPosts("",null,null,10), "", true
        )).thenAnswer {
            `when`(redditPostsLocalDS.getRedditTopPostsPaging("")).thenReturn(listOf(postLocalRemote1, postLocalRemote2).toPagingSource())
        }

        val posts = repository.getRedditTopPosts("test",10, refresh = true)
        posts.collect {
            // TODO get posts from pagingData to list using TestPager
            val resultPosts: List<RedditPost> = null!! // it.extractElementsFromPagingData

            assertEquals(2, resultPosts.size)
            assertEquals("20", resultPosts[0].fullname)
            assertEquals("TitleRemote1", resultPosts[0].title)
            assertEquals("21",resultPosts[1].fullname)
            assertEquals("TitleRemote2", resultPosts[1].title)

        }
    }
}

private fun List<RedditPostEntity>.toPagingSource(): PagingSource<Int, RedditPostEntity> = TestPagingSource(this)

class TestPagingSource(private val items: List<RedditPostEntity>) : PagingSource<Int, RedditPostEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RedditPostEntity> {
        try {
            val pageKey = params.key ?: 0
            val startingIndex = pageKey * params.loadSize
            val page = items.subList(startingIndex, startingIndex + params.loadSize)
            val nextPageKey = if ((startingIndex + params.loadSize) < items.size) pageKey + 1 else null

            return LoadResult.Page(data = page, prevKey = null, nextKey = nextPageKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RedditPostEntity>): Int? {
        // We simply return the key of the first item in the list.
        return items.firstOrNull()?.gid
    }
}