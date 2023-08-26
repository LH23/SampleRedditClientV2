package io.moonlighting.redditclientv2.ui.compose.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.ui.compose.postslist.UIRedditPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor (
    private val repository: RedditClientRepository
) : ViewModel() {
    companion object {
        private const val TAG = "PostDetailUiState"
    }

    private val _uiState: MutableStateFlow<PostDetailUiState> = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState

    init {
        viewModelScope.launch {
            getPost()
        }
    }

    private fun getPost() {
        //_uiState.value = repository.getRedditPost(fullname)
    }

}

data class PostDetailUiState (
    val redditPost: UIRedditPost? = null
)


