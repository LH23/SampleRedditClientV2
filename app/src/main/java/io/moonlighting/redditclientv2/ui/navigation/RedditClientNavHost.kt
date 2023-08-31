package io.moonlighting.redditclientv2.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.moonlighting.redditclientv2.ui.compose.postdetail.PostDetailScreen
import io.moonlighting.redditclientv2.ui.compose.postslist.PostsListScreen
import io.moonlighting.redditclientv2.ui.navigation.Routes.ARGUMENT_POST_FULLNAME

@Composable
fun RedditClientNavHost(
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.POSTS_LIST) {
        composable(Routes.POSTS_LIST) {
            PostsListScreen(
                paddingValues = paddingValues,
                onPostItemClick = { navController.navigate(Routes.getPostDetail(it.fullname)) }
            )
        }
        composable(Routes.getPostDetail("{postFullname}"),
            arguments = listOf(navArgument(ARGUMENT_POST_FULLNAME) { type = NavType.StringType })
        ) {
            PostDetailScreen(
                paddingValues = paddingValues,
                onOpenRedditLink = { redditUrl ->
                    println("opening redditUrl $redditUrl")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redditUrl))
                    context.startActivity(intent)
                }
            )
        }
    }
}

