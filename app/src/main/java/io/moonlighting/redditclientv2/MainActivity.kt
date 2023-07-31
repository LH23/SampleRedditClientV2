package io.moonlighting.redditclientv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RedditClientMainScreen(darkMode, viewModel::toggleDarkMode)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditClientMainScreen(darkMode: Boolean, toggleDarkMode: () -> Unit) {
    Scaffold(
        topBar = {
            Surface (shadowElevation = 4.dp){
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = RedditOrange,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.height(36.dp),
                                painter = painterResource(R.drawable.reddit_icon),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                fontWeight = FontWeight.SemiBold,
                                text = "RedditClient V2"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = toggleDarkMode) {
                            Icon(
                                painter = painterResource(id =
                                    if (darkMode) R.drawable.ic_light_mode
                                    else R.drawable.ic_dark_mode
                                ),
                                tint = if (darkMode) Color.DarkGray
                                    else Color.White,
                                contentDescription = stringResource(R.string.menu_toggle_dark_mode)
                            )
                        }
                    }
                )
            }
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
        RedditClientMainScreen(false) {}
    }
}