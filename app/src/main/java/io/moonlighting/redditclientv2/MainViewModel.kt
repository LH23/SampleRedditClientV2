package io.moonlighting.redditclientv2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.core.data.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: RedditClientRepository
) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
        private const val DEFAULT_SUBREDDIT= ""
        private const val PAGE_SIZE= 20
    }

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState(loading=true))
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            syncRepo()
        }
    }

    private fun syncRepo() {
        _uiState.value = MainUiState(
            redditPostsFlow = repository.getRedditTopPosts(DEFAULT_SUBREDDIT, PAGE_SIZE)
                .map { pagingData ->
                    pagingData.map { post ->
                        println("ui post: $post")
                        UIRedditPost(post)
                    }
                }
        )
    }

    fun onPostClick(post: UIRedditPost) {
        Log.d(TAG, "Clicked post: $post")
    }
}

data class MainUiState (
    val redditPostsFlow: Flow<PagingData<UIRedditPost>> = emptyFlow(),
    val loading: Boolean = false,
    val error: String? = null
)

data class UIRedditPost(
    val fullname: String,
    val title: String,
    val subredditPrefixed: String,
    val authorPrefixed: String,
    val thumbnail: String,
    val url: String,
    ) {
    constructor(post: RedditPost) : this(
        post.fullname,
        post.title,
        post.subreddit,
        "u/"+post.author,
        post.thumbnail.replace("&amp;","&"),
        post.url
    )
}

