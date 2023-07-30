package io.moonlighting.redditclientv2.ui.navigation

object Routes {
    const val POSTS_LIST = "PostsList"
    const val POST = "Post"

    fun getPostDestination(postFullname: String) = "$POST/$postFullname"

}