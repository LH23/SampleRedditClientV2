package io.moonlighting.redditclientv2.ui.compose.postslist

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import app.cash.turbine.testIn
import app.cash.turbine.turbineScope
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl.Companion.errorMessage
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl.Companion.fakePosts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
        val uiPosts = fakePosts.map { UIRedditPost(it) }
        val expected = PostsListUiState(redditPostsFlow = flowOf(PagingData.from(uiPosts)))

        val viewModel = PostsListViewModel(repository, testDispatcher)

        // When & Then
        viewModel.uiState.test {
            assertEquals(loading, awaitItem())

            Log.d("test", "hola "+expected.redditPostsFlow.toList())
            Log.d("test", "chau "+viewModel.uiState.value.redditPostsFlow.toList())
            assertEquals(
                expected.redditPostsFlow.toList(),
                awaitItem().redditPostsFlow.toList()
            )
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

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}