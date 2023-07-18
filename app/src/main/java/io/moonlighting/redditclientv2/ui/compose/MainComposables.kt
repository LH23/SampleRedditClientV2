package io.moonlighting.redditclientv2.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.moonlighting.redditclientv2.R
import io.moonlighting.redditclientv2.UIRedditPost
import kotlinx.coroutines.flow.Flow

@Composable
fun ListOfPosts(redditPostsFlow: Flow<PagingData<UIRedditPost>>, onPostClick: (UIRedditPost) -> Unit) {

    val lazyPagingItems = redditPostsFlow.collectAsLazyPagingItems()

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(
            lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it }
        ) { index ->
            val post = lazyPagingItems[index]!!
            RedditPostCard(post, onPostClick)
        }
    }
}

@Composable
fun ErrorMessage() {
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.error_loading_posts))
    }
}

@Composable
fun LoadingScreen() {
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(
            modifier = Modifier.size(size = 48.dp),
            color = Color.Red
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(text = stringResource(R.string.loading))
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RedditPostCard(post: UIRedditPost, onPostClick: (UIRedditPost) -> Unit) {
    ElevatedCard(modifier = Modifier
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

@Preview(showBackground = true)
@Composable
fun RedditPostCardPreview() {
    val post = UIRedditPost(
        "00_asdcvb",
        "This is a test post with something very amazing",
        "r/amazed", "u/lio23",
        "https://freeiconshop.com/wp-content/uploads/edd/reddit-flat.png",
        "https://freeiconshop.com/icon/reddit-icon-flat/"
    )
    RedditPostCard(post) { p -> Log.d("preview", "$p") }
}