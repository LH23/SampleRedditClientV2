package io.moonlighting.redditclientv2.ui.compose.postslist

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import io.moonlighting.redditclientv2.ui.compose.AuthorName
import io.moonlighting.redditclientv2.ui.compose.LoadingScreen
import io.moonlighting.redditclientv2.ui.compose.SubredditName
import io.moonlighting.redditclientv2.ui.compose.fakePost
import kotlinx.coroutines.flow.Flow

@Composable
internal fun ListOfRedditPosts(redditPostsFlow: Flow<PagingData<UIRedditPost>>, onPostClick: (UIRedditPost) -> Unit) {
    val lazyPagingItems = redditPostsFlow.collectAsLazyPagingItems()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
                RedditListPostCardPlaceholder()
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditListPostCardPlaceholder(
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
                SubredditName(text = post.subredditPrefixed)
                AuthorName(text = post.authorPrefixed)
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

@Preview(showBackground = true)
@Composable
fun RedditListPostCardPreview() {
    val post = fakePost
    RedditListPostCard(post) { p -> Log.d("preview", "$p") }
}
