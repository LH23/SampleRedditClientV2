package io.moonlighting.redditclientv2.ui.compose.postslist

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl.Companion.errorMessage
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl.Companion.fakePosts
import io.moonlighting.redditclientv2.ui.model.UIRedditPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostsListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var repository: RedditClientRepositoryFakeImpl

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = RedditClientRepositoryFakeImpl()
    }

    @Test
    fun `when getPosts is successful, then UiState with redditPostsFlow is emitted`() = testScope.runTest {
        // Given
        repository.addFakePosts(fakePosts)
        val loading = PostsListUiState(loading=true)
        val expected = fakePosts.map { UIRedditPost(it) }

        val results = mutableListOf<UIRedditPost>()

        // When
        turbineScope {
            val viewModel = PostsListViewModel(repository, testDispatcher)
            val turbine = viewModel.uiState.testIn(backgroundScope)
            assertEquals(loading, turbine.awaitItem())
            turbine.awaitItem()
            results.addAll(
                viewModel.uiState.value.redditPostsFlow.asSnapshot {
                    scrollTo(index = 4)
                }
            )
        }

        // then
        assertEquals(
            expected,
            results
        )
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

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}