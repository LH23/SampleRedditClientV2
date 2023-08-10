package io.moonlighting.redditclientv2.core.data.local

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryImpl
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.model.RedditPost
import io.moonlighting.redditclientv2.core.data.remote.FakeRedditPostsRemoteDS
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import java.util.logging.Level.CONFIG


class RedditPostsLocalDSImplTest {

    private lateinit var redditLocalDS: RedditPostsLocalDSImpl

    private val postLocal1 = RedditPostEntity(null, "10","TitleLocal1","","","","")
    private val postLocal2 = RedditPostEntity(null, "11","TitleLocal2","","","","")
    private val postRemote1 = RedditPostRemote("20","TitleRemote1","","","","")
    private val postRemote2 = RedditPostRemote("21","TitleRemote2","","","","")

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setup() {
        val fakeDao: RedditPostsDao = FakeDao(mutableListOf(postLocal1, postLocal2))
        redditLocalDS = RedditPostsLocalDSImpl(fakeDao)
    }

    @Test
    fun getRedditTopPostsPaging() {
        // given

        //when
        redditLocalDS.getRedditTopPostsPaging("")

        //then

    }

    @Test
    fun loadReturnsTheApiPageAfterRefresh() = runTest {
        // TODO
//        val pagingSource = ...
//        val pager = TestPager(CONFIG, pagingSource)
//
//        val result = pager.refresh() as LoadResult.Page
//
//        assertThat(result.data)
//            .containsExactlyElementsIn(mockPosts)
//            .inOrder()
    }

    @Test
    fun `updateRedditLocalPosts with refresh false appends them`() = testScope.runTest {
        // given

        //when
        redditLocalDS.updateRedditLocalPosts(listOf(postRemote1, postRemote2), "", false)

        //then

    }

    @Test
    fun `updateRedditLocalPosts with refresh true clears the list before adding them`() = testScope.runTest {
        // given

        //when
        redditLocalDS.updateRedditLocalPosts(listOf(postRemote1, postRemote2), "", true)

        //then

    }


    @Test
    fun removeAllSavedPosts() = testScope.runTest {
        // given

        //when
        redditLocalDS.removeAllSavedPosts("")

        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")
        val pager = TestPager(PagingConfig(pageSize=2), pagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page

        // then
        assertEquals(result.data, emptyList<RedditPostEntity>())

    }
}