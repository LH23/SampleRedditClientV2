package io.moonlighting.redditclientv2.ui.compose.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.ui.model.UIRedditPost
import io.moonlighting.redditclientv2.ui.navigation.Routes.ARGUMENT_POST_FULLNAME
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val repository: RedditClientRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    companion object {
        private const val TAG = "PostDetailUiState"
    }

    private val _uiState: MutableStateFlow<PostDetailUiState> = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState

    private val postFullname: String = checkNotNull(savedStateHandle[ARGUMENT_POST_FULLNAME])
    init {
        getPost(postFullname)
    }

    private fun getPost(fullname: String?) {
        if (fullname != null) {
            viewModelScope.launch(dispatcher) {
                repository.getRedditPost(fullname).map { UIRedditPost(it) }.collect { post ->
                    _uiState.value = PostDetailUiState(post)
                }
            }
        }

    }

    fun savePost() {
        // TODO
    }

    fun sharePost() {
        // TODO
    }

    private val _redditLinkState: MutableStateFlow<String?> = MutableStateFlow(null)
    val redditLinkState: StateFlow<String?> = _redditLinkState

    fun openPost() {
        _redditLinkState.value = _uiState.value.redditPost?.redditLink
    }

    fun onLinkOpenComplete() {
        _redditLinkState.value = null
    }

}

data class PostDetailUiState (
    val redditPost: UIRedditPost? = null
)


