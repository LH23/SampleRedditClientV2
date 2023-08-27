package io.moonlighting.redditclientv2.ui.compose.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import dagger.hilt.android.lifecycle.HiltViewModel
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.ui.compose.postslist.UIRedditPost
import io.moonlighting.redditclientv2.ui.navigation.Routes.ARGUMENT_POST_FULLNAME
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s
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
}

data class PostDetailUiState (
    val redditPost: UIRedditPost? = null
)


