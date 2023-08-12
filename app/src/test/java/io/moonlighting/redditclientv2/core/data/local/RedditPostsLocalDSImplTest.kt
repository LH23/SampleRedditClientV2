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
        assertEquals(result.data, entityPosts)
    }

    @Test
    fun `getRedditTopPostsPaging with no elements returns an empty page`() = runTest {
        // given
        // the fakeDao is empty (by default)

        //when
        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")

        //then
        val result = pagingSource.getPage()
        assertEquals(result.data, emptyList<RedditPostEntity>())
    }

    @Test
    fun `updateRedditLocalPosts with refresh false appends them`() = testScope.runTest {
        // given
        fakeDao.addAll(entityPosts)
        val expected = entityPosts+remotePosts.map { RedditPostEntity(it) }

        //when
        redditLocalDS.updateRedditLocalPosts(remotePosts, "", false)

        //then
        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")
        val result = pagingSource.getPage(size = 20)
        // check only by fullname because creation time is different
        assertEquals(expected.map { it.fullname }, result.data.map{ it.fullname })
    }

    @Test
    fun `updateRedditLocalPosts with refresh true clears the list before adding them`() = testScope.runTest {
        // given
        fakeDao.addAll(entityPosts)
        val expected = remotePosts.map { RedditPostEntity(it) }

        //when
        redditLocalDS.updateRedditLocalPosts(remotePosts, "", true)

        //then
        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")
        val result = pagingSource.getPage()
        assertEquals(expected, result.data)
    }

    @Test
    fun `remove all saved posts works`() = testScope.runTest {
        // given
        fakeDao.addAll(entityPosts)

        //when
        redditLocalDS.removeAllSavedPosts("")

        // then
        val pagingSource = redditLocalDS.getRedditTopPostsPaging("")
        val result = pagingSource.getPage()
        assertEquals(result.data, emptyList<RedditPostEntity>())
    }
}

private suspend fun <K : Any, V : Any> PagingSource<K, V>.getPage(size: Int = 10): Page<K,V> {
    val pager = TestPager(PagingConfig(pageSize = size), this)
    return pager.refresh() as Page
}
