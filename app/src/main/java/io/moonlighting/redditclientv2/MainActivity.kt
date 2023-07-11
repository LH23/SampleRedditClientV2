package io.moonlighting.redditclientv2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.moonlighting.redditclientv2.MainActivityUiState.Error
import io.moonlighting.redditclientv2.MainActivityUiState.Loading
import io.moonlighting.redditclientv2.MainActivityUiState.Success
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl
import io.moonlighting.redditclientv2.ui.theme.RedditClientV2Theme
import io.moonlighting.redditclientv2.ui.theme.RedditOrange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val app: RedditClientApp = this.application as RedditClientApp
        val factory = MainViewModel.Factory(app.repository)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setContent {
            RedditClientV2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RedditClientActivityScreen(viewModel, splashScreen)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditClientActivityScreen(
    viewModel: MainViewModel,
    splashScreen: SplashScreen?
) {

    val uiState by viewModel.uiState.collectAsState(initial = Loading)

    splashScreen?.setKeepOnScreenCondition {
        when (uiState) {
            is Loading -> true
            is Success -> false
            is Error -> false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = RedditOrange,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text("RedditClient V2")
                })
        }
    ) {
        Surface (modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            when (uiState) {
                is Loading -> { LoadingScreen() }
                is Success -> { ListOfPosts((uiState as Success).redditPosts, viewModel::onPostClick)}
                is Error -> { ErrorMessage() }
            }
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

@Composable
fun ListOfPosts(redditPosts: List<UIRedditPost>, onPostClick: (UIRedditPost) -> Unit) {
    LazyColumn (verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(redditPosts) { post ->
            RedditPostCard(post, onPostClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RedditPostCard(post: UIRedditPost, onPostClick: (UIRedditPost) -> Unit) {
    ElevatedCard (modifier = Modifier
        .fillMaxWidth()
        .heightIn(64.dp, 256.dp),
        onClick = {
            onPostClick(post)
        }
        ){
        Row (modifier = Modifier.padding(12.dp),){
            Column (modifier = Modifier.weight(1f)) {
                //Image()
                Text(text = post.subredditPrefixed, fontSize = 8.sp, fontStyle = FontStyle.Italic)
                Text(text = post.authorPrefixed, fontSize = 8.sp, fontStyle = FontStyle.Italic, color = Color.Gray)
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
    val post = UIRedditPost("00_asdcvb",
        "This is a test post with something very amazing", "r/amazed", "u/lio23",
        "https://freeiconshop.com/wp-content/uploads/edd/reddit-flat.png", "https://freeiconshop.com/icon/reddit-icon-flat/")
    RedditPostCard(post) { p -> Log.d("preview", "$p") }
}

//@Preview(showBackground = true)
@Composable
fun RedditClientActivityScreenPreview() {
    RedditClientV2Theme {
        val fakeViewModel = MainViewModel(RedditClientRepositoryFakeImpl())
        RedditClientActivityScreen(fakeViewModel, null)
    }
}