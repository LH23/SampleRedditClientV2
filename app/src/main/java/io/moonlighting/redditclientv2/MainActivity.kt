package io.moonlighting.redditclientv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.moonlighting.redditclientv2.ui.compose.RedditClientMainScreen
import io.moonlighting.redditclientv2.ui.compose.postslist.PostsListViewModel
import io.moonlighting.redditclientv2.ui.theme.RedditClientV2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val darkMode = viewModel.useDarkMode.collectAsState().value
            val loading = viewModel.uiState.collectAsState().value.loading
            splashScreen.setKeepOnScreenCondition {
                loading
            }

            RedditClientV2Theme(useDarkTheme = darkMode) {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    RedditClientMainScreen(darkMode, Modifier, viewModel::toggleDarkMode)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RedditClientActivityScreenPreview() {
    RedditClientV2Theme {
        RedditClientMainScreen(false) {}
    }
}