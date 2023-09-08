package io.moonlighting.redditclientv2.ui.model

import io.moonlighting.redditclientv2.core.data.model.RedditPost
import io.moonlighting.redditclientv2.ui.compose.postslist.formatDate

data class UIRedditPost(
    val fullname: String,
    val title: String,
    val subredditPrefixed: String,
    val subredditIconUrl: String?,
    val authorPrefixed: String,
    val contentText: String,
    val thumbnail: String,
    val url: String,
    val redditLink: String,
    val type: RedditPostType,
    val creationDateFormatted: String
) {
    constructor(post: RedditPost) : this(
        fullname = post.fullname,
        title = post.title,
        subredditPrefixed = post.subreddit,
        subredditIconUrl = post.subredditIcon,
        authorPrefixed = "u/"+post.author,
        contentText = post.contentText,
        thumbnail = post.thumbnail.replace("&amp;","&"),
        url = post.url,
        redditLink = post.redditLink,
        type = getType(post.postHint),
        creationDateFormatted = formatDate(post.creationDate)
    )
}

private fun getType(hint: String?): RedditPostType {
    println("post hint $hint")
    return when (hint) {
        "image" -> RedditPostType.IMAGE_POST
        "hosted:video" -> RedditPostType.VIDEO_POST
        "link" -> RedditPostType.WEB_POST
        null -> RedditPostType.TEXT_POST
        else -> RedditPostType.IMAGE_POST // TODO check this default
    }
}

enum class RedditPostType {
    IMAGE_POST,
    VIDEO_POST,
    TEXT_POST,
    WEB_POST
}


val fakePost = UIRedditPost(
    fullname = "00_asdcvb",
    title = "This is a test post with something very amazing",
    subredditPrefixed = "r/amazed",
    subredditIconUrl = "https://freeiconshop.com/wp-content/uploads/edd/reddit-flat.png",
    authorPrefixed = "u/lio23",
    contentText = "",
    thumbnail = "https://freeiconshop.com/wp-content/uploads/edd/reddit-flat.png",
    url = "https://freeiconshop.com/icon/reddit-icon-flat/",
    redditLink = "https://reddit.com",
    type = RedditPostType.IMAGE_POST,
    creationDateFormatted = "12/8/2023 03:55pm"
)