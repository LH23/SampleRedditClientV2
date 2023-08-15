package io.moonlighting.redditclientv2.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.moonlighting.redditclientv2.ui.compose.postdetail.PostDetailScreen
import io.moonlighting.redditclientv2.ui.compose.postslist.PostsListScreen

@Composable
fun RedditClientNavHost(
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Routes.POSTS_LIST) {
        composable(Routes.POSTS_LIST) {
            PostsListScreen(
                paddingValues,
            ) {
                navController.navigate(Routes.getPostDetail(it.fullname))
            }
        }
        composable(Routes.getPostDetail("{postFullname}"),
            arguments = listOf(navArgument("postFullname") {
                type = NavType.StringType
            })
        ) {
            PostDetailScreen(
                paddingValues = paddingValues,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}

