package io.moonlighting.redditclientv2.ui.compose.postslist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.moonlighting.redditclientv2.ui.compose.ErrorMessage
import io.moonlighting.redditclientv2.ui.compose.ListOfPosts
import io.moonlighting.redditclientv2.ui.compose.LoadingScreen

@Composable
fun PostsListScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    postsListViewModel: PostsListViewModel = hiltViewModel()
) {
    val uiState by postsListViewModel.uiState.collectAsState(
        initial = PostsListUiState(loading = true)
    )

    Surface(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        when {
            uiState.loading -> {
                LoadingScreen()
            }
            uiState.error != null -> {
                ErrorMessage()
            } // TODO pass the error message or add an errortype enum
            else -> {
                ListOfPosts(uiState.redditPostsFlow, postsListViewModel::onPostClick)
            }
        }
    }
}