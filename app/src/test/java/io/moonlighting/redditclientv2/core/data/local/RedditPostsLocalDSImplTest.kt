package io.moonlighting.redditclientv2.core.data.local

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.testing.TestPager
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import java.util.logging.Level.CONFIG


class RedditPostsLocalDSImplTest {

    // test subject
    private lateinit var redditLocalDS: RedditPostsLocalDSImpl

    private lateinit var fakeDao: FakeDao
    private val entityPosts: List<RedditPostEntity> = (0 until 10).map {
        RedditPostEntity(null, "name$it","Fake Title $it","","","","")
    }
    private val remotePosts: List<RedditPostRemote> = (0 until 10).map {
        RedditPostRemote("remotename$it","Remote Fake Title $it","","","","")
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setup() {
        fakeDao = FakeDao(mutableListOf())
        redditLocalDS = RedditPostsLocalDSImpl(fakeDao)
    }

    @Test
    fun `getRedditTopPostsPaging with elements returns the elements`() = runTest {
        // given
        fakeDao.addAll(entityPosts)

        //when
        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")

        //then
        val result = pagingSource.getPage()

        // Write assertions against the loaded data
        assertEquals(result.data, entityPosts)
    }

    @Test
    fun `getRedditTopPostsPaging with no elements returns an empty page`() = runTest {
        // given the fakeDao is empty (by default)

        //when
        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")

        //then

    }

    @Test
    fun `load Returns the remote API page after refresh`() = runTest {
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
        redditLocalDS.updateRedditLocalPosts(remotePosts, "", false)

        //then

    }

    @Test
    fun `updateRedditLocalPosts with refresh true clears the list before adding them`() = testScope.runTest {
        // given

        //when
        redditLocalDS.updateRedditLocalPosts(remotePosts, "", true)

        //then

    }


    @Test
    fun `remove all saved posts works`() = testScope.runTest {
        // given

        //when
        redditLocalDS.removeAllSavedPosts("")

        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")
        val result = pagingSource.getPage()

        // then
        assertEquals(result.data, emptyList<RedditPostEntity>())

    }
}

private suspend fun <K : Any, V : Any> PagingSource<K, V>.getPage(): Page {
    val pager = TestPager(PagingConfig(pageSize = 10), this)
    return pager.refresh() as Page
}
