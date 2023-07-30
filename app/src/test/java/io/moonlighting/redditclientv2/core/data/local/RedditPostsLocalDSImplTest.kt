package io.moonlighting.redditclientv2.core.data.local

import androidx.paging.testing.TestPager
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import java.util.logging.Level.CONFIG


class RedditPostsLocalDSImplTest {
//    private val mockPosts = ...
//    private val fakeApi = ...

    @Test
    fun getRedditTopPostsPaging() {
        // TODO
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
    fun updateRedditLocalPosts() {
        // TODO
    }

    @Test
    fun removeAllSavedPosts() {
        // TODO
    }
}