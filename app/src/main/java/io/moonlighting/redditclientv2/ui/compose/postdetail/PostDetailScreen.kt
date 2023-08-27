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
        when (uiState.redditPost) {
            null -> {
               // error message
            }
            else -> {
               Text(uiState.redditPost!!.title)
                // image post
                // video post
                // url post
            }
        }
    }
}