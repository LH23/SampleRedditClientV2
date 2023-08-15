package io.moonlighting.redditclientv2.ui.compose.postdetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.moonlighting.redditclientv2.ui.compose.ErrorMessage
import io.moonlighting.redditclientv2.ui.compose.ListOfPosts
import io.moonlighting.redditclientv2.ui.compose.LoadingScreen
import io.moonlighting.redditclientv2.ui.compose.postslist.PostsListUiState
import io.moonlighting.redditclientv2.ui.compose.postslist.PostsListViewModel

@Composable
fun PostDetailScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    postDetailViewModel: PostDetailViewModel = hiltViewModel(),
    onBackClick: () -> Boolean
) {
    val uiState by postDetailViewModel.uiState.collectAsState(
        initial = PostDetailUiState()
    )

    Surface(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        when {
            uiState.redditPost != null -> {
                Text(uiState.redditPost!!.title)
                // image post
                // video post
                // url post
            }
            else -> {
                // error message
            }
        }
    }
}