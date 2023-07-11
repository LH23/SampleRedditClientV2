package io.moonlighting.redditclientv2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.moonlighting.redditclientv2.MainActivityUiState.*
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.core.data.RedditPost
import io.moonlighting.redditclientv2.core.data.RepoResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RedditClientRepository) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    class Factory(private val parameter: RedditClientRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(parameter) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }
    }

    private val _uiState: MutableStateFlow<MainActivityUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState

    init {
        viewModelScope.launch {
            syncRepo()
        }
    }

    private suspend fun syncRepo() {
        repository.getRedditTopPosts().await().map { result ->
            when (result) {
                is RepoResult.Error -> Error("Loading error")
                is RepoResult.Loading -> Loading
                is RepoResult.Success -> Success(result.posts.map { post ->
                    UIRedditPost(post)
                })
            }
        }.collect {
            _uiState.value = it
        }
    }

    fun onPostClick(post: UIRedditPost) {
        Log.d(TAG, "Clicked post: $post")
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val redditPosts: List<UIRedditPost>) : MainActivityUiState
    data class Error(val error: String) : MainActivityUiState
}


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

