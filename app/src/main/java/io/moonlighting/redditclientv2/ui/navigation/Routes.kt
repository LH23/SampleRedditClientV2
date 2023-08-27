package io.moonlighting.redditclientv2.ui.navigation

object Routes {
    const val POSTS_LIST = "PostsList"
    const val POST = "Post"

    const val ARGUMENT_POST_FULLNAME = "postFullname"

    fun getPostDetail(postFullname: String) = "$POST/$postFullname"

}