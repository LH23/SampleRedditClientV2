package io.moonlighting.redditclientv2.ui.compose.postdetail

import android.net.Uri
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.moonlighting.redditclientv2.R
import io.moonlighting.redditclientv2.ui.compose.AuthorName
import io.moonlighting.redditclientv2.ui.compose.ErrorMessage
import io.moonlighting.redditclientv2.ui.compose.SubredditName
import io.moonlighting.redditclientv2.ui.compose.utils.VideoPlayer
import io.moonlighting.redditclientv2.ui.compose.utils.WebViewPage
import io.moonlighting.redditclientv2.ui.model.RedditPostType
import io.moonlighting.redditclientv2.ui.model.UIRedditPost
import io.moonlighting.redditclientv2.ui.model.fakePost

@Composable
fun RedditDetailsPostCard(post: UIRedditPost,
                          savePost: () -> Unit,
                          sharePost: () -> Unit,
                          openPost: () -> Unit,
                          modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier
        .fillMaxWidth()
    ) {
        Column {
            PostCardHeader(post)
            when (post.type) {
                RedditPostType.IMAGE_POST -> { FullSizedImage(post.url, modifier = Modifier.weight(1f)) }
                RedditPostType.VIDEO_POST -> { FullSizedVideo(post.url, modifier = Modifier.weight(1f)) }
                RedditPostType.WEB_POST -> { FullSizedWebView(post.url, modifier = Modifier.weight(1f))}
                RedditPostType.TEXT_POST -> { FullSizedText(post.contentText, modifier = Modifier.weight(1f)) }
                else -> { ErrorMessage(stringResource(R.string.error_invalid_post)) }
            }
            PostCardFooter(savePost, sharePost, openPost)
        }
    }
}

@Composable
fun PostCardHeader(post: UIRedditPost, modifier: Modifier = Modifier) {
    Row (modifier = modifier
        .fillMaxWidth()
        .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubredditIcon(post.subredditIconUrl)
        Column (modifier = Modifier.weight(1f)){
            SubredditName(text = post.subredditPrefixed, size = 12)
            AuthorName(text = post.authorPrefixed, size = 12)
        }
        SubredditName(text = post.creationDateFormatted, size = 12, modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubredditIcon(iconUrl: String?, modifier: Modifier = Modifier) {
    GlideImage(
        model = iconUrl,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(56.dp)
            .padding(8.dp),
        contentDescription = null
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullSizedImage(imageUrl: String, modifier: Modifier = Modifier) {
    GlideImage(
        model = imageUrl,
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentDescription = null
    )
}

@Composable
fun FullSizedVideo(videoUrl: String, modifier: Modifier = Modifier) {
    println("UI VIDEO URL $videoUrl")
    VideoPlayer(
        videoUri = Uri.parse(videoUrl),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun FullSizedWebView(websiteUrl: String, modifier: Modifier = Modifier) {
    WebViewPage(
        url = websiteUrl,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}

@Composable
fun FullSizedText(contentText: String, modifier: Modifier = Modifier) {
    val state = rememberScrollState()
    Text(
        contentText,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(state)
    )
}

@Composable
fun PostCardFooter(
        savePost: () -> Unit,
        sharePost: () -> Unit,
        openPost: () -> Unit,
        modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ){
        Button(content = { Text("Share") }, onClick = { savePost() })
        Button(content = { Text("Save") }, onClick = { sharePost() })
        Button(content = { Text("Open") }, onClick = { openPost() })
    }
}

@Preview(showBackground = true)
@Composable
fun RedditPostCardPreview() {
    val post = fakePost
    RedditDetailsPostCard(post, {}, {}, {})
}
