package io.moonlighting.redditclientv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.moonlighting.redditclientv2.ui.compose.postslist.PostsListViewModel
import io.moonlighting.redditclientv2.ui.navigation.RedditClientNavHost
import io.moonlighting.redditclientv2.ui.theme.RedditClientV2Theme
import io.moonlighting.redditclientv2.ui.theme.RedditOrange

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            RedditClientV2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RedditClientMainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditClientMainScreen() {
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
        val navController = rememberNavController()
        RedditClientNavHost(it, navController)
    }
}


@Preview(showBackground = true)
@Composable
fun RedditClientActivityScreenPreview() {
    RedditClientV2Theme {
        RedditClientMainScreen()
    }
}