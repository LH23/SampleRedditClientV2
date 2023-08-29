package io.moonlighting.redditclientv2.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.moonlighting.redditclientv2.R
import io.moonlighting.redditclientv2.ui.compose.postslist.RedditPostType
import io.moonlighting.redditclientv2.ui.compose.postslist.UIRedditPost
import kotlinx.coroutines.flow.Flow



@Composable
internal fun ListOfPosts(redditPostsFlow: Flow<PagingData<UIRedditPost>>, onPostClick: (UIRedditPost) -> Unit) {

    val lazyPagingItems = redditPostsFlow.collectAsLazyPagingItems()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                LoadingScreen()
            }
        }

        items(
            lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.fullname }
        ) { index ->
            val post = lazyPagingItems[index]
            if (post != null) {
                LaunchedEffect(key1 = post) {
                    println("post $index added to the UI: $post")
                }
                RedditListPostCard(post, Modifier, onPostClick)
            } else {
                RedditPostCardPlaceholder()
            }
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

    }
}


@Composable
fun ErrorMessage(
    error: String,
    modifier : Modifier = Modifier
) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = error)
    }
}

@Composable
fun LoadingScreen(
    modifier : Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(
            modifier = Modifier.size(size = 48.dp),
            color = Color.Red
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(text = stringResource(R.string.loading))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditPostCardPlaceholder(
    modifier : Modifier = Modifier
) {
    ElevatedCard(modifier = modifier
        .fillMaxWidth()
        .height(64.dp),
        onClick = {}
    ) {}
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RedditListPostCard(post: UIRedditPost,
                       modifier: Modifier = Modifier,
                       onPostClick: (UIRedditPost) -> Unit
) {
    ElevatedCard(modifier = modifier
        .fillMaxWidth()
        .heightIn(64.dp, 256.dp),
        onClick = {
            onPostClick(post)
        }
    ) {
        Row(modifier = Modifier.padding(12.dp),) {
            Column(modifier = Modifier.weight(1f)) {
                //Image()
                Text(text = post.subredditPrefixed, fontSize = 8.sp, fontStyle = FontStyle.Italic)
                Text(
                    text = post.authorPrefixed,
                    fontSize = 8.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )
                Text(text = post.title, fontSize = 16.sp)
            }
            if (post.thumbnail.isNotEmpty()) {
                GlideImage(
                    model = post.thumbnail,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.CenterVertically),
                    contentDescription = stringResource(id = R.string.post_thumbnail)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RedditDetailsPostCard(post: UIRedditPost,
                          modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier
        .fillMaxWidth()
    ) {
        Column {
            PostCardHeader(post)
            when (post.type) {
                RedditPostType.IMAGE_POST -> { FullSizedImage(post.url) }
//                VIDEO_POST -> {}
//                WEB_POST -> {}
//                TEXT_POST -> {}
                else -> { ErrorMessage(stringResource(R.string.error_invalid_post)) }
            }
            PostCardFooter(post)
        }
    }
}

@Composable
fun PostCardHeader(post: UIRedditPost, modifier: Modifier = Modifier) {
    Row (modifier = modifier.fillMaxWidth().height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubredditIcon(post.subredditIconUrl)
        Column (modifier = Modifier.weight(1f)){
            Text(text = post.subredditPrefixed)
            Text(text = post.authorPrefixed)
        }
        Text(text = post.creationDateFormatted)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubredditIcon(iconUrl: String, modifier: Modifier = Modifier) {
    GlideImage(
        model = iconUrl,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(56.dp),
        contentDescription = null
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullSizedImage(imageUrl: String, modifier: Modifier = Modifier) {
    GlideImage(
        model = imageUrl,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth().height(400.dp),
        contentDescription = null
    )
}

@Composable
fun PostCardFooter(post: UIRedditPost, modifier: Modifier = Modifier) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ){
        Button(content = { Text("Share")}, onClick = {})
        Button(content = { Text("Save")}, onClick = {})
        Button(content = { Text("Open")}, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun RedditPostCardPreview() {
    val post = fakePost
    RedditDetailsPostCard(post)
}

@Preview(showBackground = true)
@Composable
fun RedditListPostCardPreview() {
    val post = fakePost
    RedditListPostCard(post) { p -> Log.d("preview", "$p") }
}

val fakePost = UIRedditPost(
    "00_asdcvb",
    "This is a test post with something very amazing",
    "r/amazed",
    "https://freeiconshop.com/wp-content/uploads/edd/reddit-flat.png",
    "u/lio23",
    "https://freeiconshop.com/wp-content/uploads/edd/reddit-flat.png",
    "https://freeiconshop.com/icon/reddit-icon-flat/",
    RedditPostType.IMAGE_POST,
    "12/8/2023 03:55pm"
)