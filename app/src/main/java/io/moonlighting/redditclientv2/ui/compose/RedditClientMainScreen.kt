package io.moonlighting.redditclientv2.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.moonlighting.redditclientv2.R
import io.moonlighting.redditclientv2.ui.navigation.RedditClientNavHost
import io.moonlighting.redditclientv2.ui.navigation.Routes
import io.moonlighting.redditclientv2.ui.theme.RedditOrange

@Composable
fun RedditClientMainScreen(
    darkMode: Boolean,
    modifier: Modifier = Modifier,
    toggleDarkMode: () -> Unit
) {
    val navController = rememberNavController()
    var showAppBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    showAppBar = when (navBackStackEntry?.destination?.route) {
        Routes.POSTS_LIST -> true
        else -> false
    }

    Scaffold(
        modifier = modifier,
        topBar = { if (showAppBar) RedditTopAppBar(darkMode, toggleDarkMode) }
    ) {
        RedditClientNavHost(it, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditTopAppBar(darkMode: Boolean, toggleDarkMode: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shadowElevation = 4.dp
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
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
                        painter = painterResource(
                            id =
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