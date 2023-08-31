package io.moonlighting.redditclientv2.ui.compose.postdetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import io.moonlighting.redditclientv2.R
import io.moonlighting.redditclientv2.ui.compose.ErrorMessage

@Composable
fun PostDetailScreen(
    paddingValues: PaddingValues,
    onOpenRedditLink: (String) -> Unit,
    modifier: Modifier = Modifier,
    postDetailViewModel: PostDetailViewModel = hiltViewModel()
) {
    val uiState by postDetailViewModel.uiState.collectAsState(
        initial = PostDetailUiState()
    )

    postDetailViewModel.redditLinkState.collectAsState().value?.let { url ->
        onOpenRedditLink(url)
        postDetailViewModel.onLinkOpenComplete()
    }

    Surface(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        when (uiState.redditPost) {
            null -> {
                ErrorMessage(stringResource(R.string.error_post_not_found))
            }
            else -> {
                RedditDetailsPostCard(
                    uiState.redditPost!!,
                    postDetailViewModel::savePost,
                    postDetailViewModel::sharePost,
                    postDetailViewModel::openPost
                )
                // image post
                // video post
                // url post
            }
        }
    }
}