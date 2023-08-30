package io.moonlighting.redditclientv2.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.moonlighting.redditclientv2.R
import io.moonlighting.redditclientv2.ui.compose.postslist.RedditPostType
import io.moonlighting.redditclientv2.ui.compose.postslist.UIRedditPost


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

@Composable
fun SubredditName(text: String, modifier: Modifier = Modifier, size: Int = 8) {
    Text(text, fontSize = size.sp, fontStyle = FontStyle.Italic, modifier = modifier)
}
@Composable
fun AuthorName(text: String, modifier: Modifier = Modifier, size: Int = 8) {
    Text(text, fontSize = size.sp, fontStyle = FontStyle.Italic, color = Color.Gray, modifier = modifier)
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