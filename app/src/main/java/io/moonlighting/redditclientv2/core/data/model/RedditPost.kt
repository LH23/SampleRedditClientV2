package io.moonlighting.redditclientv2.core.data.model

import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import java.util.Date

data class RedditPost(
    val fullname: String,
    val title: String,
    val subreddit: String,
    val subredditIcon: String?,
    val author: String,
    val thumbnail: String,
    val contentText: String,
    val url: String,
    val redditLink: String,
    val postHint: String?,
    val creationDate: Date,
) {
    constructor(postEntity: RedditPostEntity) :
            this(
                fullname = postEntity.fullname,
                title = postEntity.title,
                subreddit = postEntity.subredditName,
                subredditIcon = postEntity.subredditIcon,
                author = postEntity.author,
                thumbnail = postEntity.thumbnail,
                url = postEntity.sourceUrl,
                contentText = postEntity.contentText,
                redditLink = postEntity.redditLink,
                postHint = postEntity.postHint,
                creationDate = Date(postEntity.createdUtc*1000)
            )
}