package io.moonlighting.redditclientv2.core.data.model

import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import java.util.Date

data class RedditPost(
    val fullname: String,
    val title: String,
    val subreddit: String,
    val author: String,
    val thumbnail: String,
    val url: String,
    val creationDate: Date,
) {


    constructor(postEntity: RedditPostEntity) :
            this(
                postEntity.fullname,
                postEntity.title,
                postEntity.subreddit,
                postEntity.author,
                postEntity.thumbnail,
                postEntity.sourceUrl,
                Date(postEntity.createdUtc)
            )
}