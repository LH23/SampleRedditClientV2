package io.moonlighting.redditclientv2.ui.compose.postslist

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl.Companion.errorMessage
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl.Companion.fakePosts
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PostsListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var repository: RedditClientRepositoryFakeImpl

    @BeforeEach
    fun setup() {
        repository = RedditClientRepositoryFakeImpl()
    }

    @Test
    fun `when getPosts is successful, then UiState with redditPostsFlow is emitted`() = testScope.runTest {
        // Given
        repository.addFakePosts(fakePosts)
        val loading = PostsListUiState(loading=true)
        val uiPosts = fakePosts.map { UIRedditPost(it) }
        val expected = PostsListUiState(redditPostsFlow = flowOf(PagingData.from(uiPosts)))

        // When & Then
        val viewModel = PostsListViewModel(repository, testDispatcher)
        viewModel.uiState.test {
            assertEquals(loading, awaitItem())
            assertEquals( // TODO fix this test assert
                expected.redditPostsFlow,
                awaitItem().redditPostsFlow
            )
            awaitComplete()
        }
    }

    @Test
    fun `when getPosts fails, then UiState with error is emitted`() = testScope.runTest {
        // Given
        repository.setErrorOnGetPosts(true)
        val loading = PostsListUiState(loading = true)
        val expected = PostsListUiState(error = errorMessage)

        // When & Then
        val viewModel = PostsListViewModel(repository, testDispatcher)
        viewModel.uiState.test {
            assertEquals(loading, awaitItem())
            assertEquals(expected, awaitItem())
        }
    }
}