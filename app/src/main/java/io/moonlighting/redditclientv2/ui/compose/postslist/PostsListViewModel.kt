package io.moonlighting.redditclientv2.ui.compose.postslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.ui.model.UIRedditPost
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor (
    private val repository: RedditClientRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    companion object {
        private const val TAG = "PostsListViewModel"
        private const val DEFAULT_SUBREDDIT= ""
        private const val PAGE_SIZE= 20
    }

    private var _useDarkMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val useDarkMode: StateFlow<Boolean> = _useDarkMode

    private val _uiState: MutableStateFlow<PostsListUiState> = MutableStateFlow(PostsListUiState(loading=true))
    val uiState: StateFlow<PostsListUiState> = _uiState

    init {
        viewModelScope.launch(dispatcher) {
            syncRepo()
        }
    }

    private fun syncRepo() {
        try {
            _uiState.value = PostsListUiState(
                redditPostsFlow = repository.getRedditTopPosts(DEFAULT_SUBREDDIT, PAGE_SIZE)
                    .map { pagingData ->
                        pagingData.map { post ->
                            println("ui post: $post")
                            UIRedditPost(post)
                        }
                    }.cachedIn(viewModelScope),
                loading = false
            )
        } catch (e: Exception) {
            _uiState.value = PostsListUiState(error = e.message ?: "Error")
        }
    }

    fun onPostClick(post: UIRedditPost) {
        // UNUSED
        Log.d(TAG, "Clicked post: $post")
    }

    fun toggleDarkMode() {
        _useDarkMode.value = !_useDarkMode.value
    }
}

data class PostsListUiState (
    val redditPostsFlow: Flow<PagingData<UIRedditPost>> = emptyFlow(),
    val loading: Boolean = false,
    val error: String? = null
)

fun formatDate(creationDate: Date): String {
    val formatter = SimpleDateFormat("MMM dd HH:mm", Locale.US)
    return formatter.format(creationDate)
}

