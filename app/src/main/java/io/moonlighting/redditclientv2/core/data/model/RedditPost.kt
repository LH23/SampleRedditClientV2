package io.moonlighting.redditclientv2.core.data.model

import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import java.time.LocalDate
import java.util.Date

data class RedditPost(
    val fullname: String,
    val title: String,
    val subreddit: String,
    val author: String,
    val thumbnail: String,
    val url: String,
    val creationDate: LocalDate,
) {


    constructor(postEntity: RedditPostEntity) :
            this(
                postEntity.fullname,
                postEntity.title,
                postEntity.subreddit,
                postEntity.author,
                postEntity.thumbnail,
                postEntity.sourceUrl,
                LocalDate.ofEpochDay(postEntity.createdUtc)
            )
}