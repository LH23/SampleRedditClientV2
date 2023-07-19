package io.moonlighting.redditclientv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryFakeImpl
import io.moonlighting.redditclientv2.ui.compose.ErrorMessage
import io.moonlighting.redditclientv2.ui.compose.ListOfPosts
import io.moonlighting.redditclientv2.ui.compose.LoadingScreen
import io.moonlighting.redditclientv2.ui.theme.RedditClientV2Theme
import io.moonlighting.redditclientv2.ui.theme.RedditOrange

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

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

    val uiState by viewModel.uiState.collectAsState(initial = MainUiState(loading = true))

    splashScreen?.setKeepOnScreenCondition{ uiState.loading }

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
            when {
                uiState.loading -> { LoadingScreen() }
                uiState.error != null -> { ErrorMessage() } // TODO pass the error message or add an errortype enum
                else -> {
                    ListOfPosts(uiState.redditPostsFlow, viewModel::onPostClick)
                }
            }
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun RedditClientActivityScreenPreview() {
    RedditClientV2Theme {
        val fakeViewModel = MainViewModel(RedditClientRepositoryFakeImpl())
        RedditClientActivityScreen(fakeViewModel, null)
    }
}