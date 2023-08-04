package io.moonlighting.redditclientv2.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.moonlighting.redditclientv2.core.data.remote.RedditPostRemote

@Entity(tableName = "redditposts")
data class RedditPostEntity(
    @PrimaryKey(autoGenerate = true)
    val gid: Int?, // to keep the list ordered
    @ColumnInfo(name = "fullname")
    val fullname: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "subreddit")
    val subreddit: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "sourceUrl")
    val sourceUrl: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor(redditPostRemote: RedditPostRemote) : this(
        null,
        redditPostRemote.fullname,
        redditPostRemote.title,
        redditPostRemote.author,
        redditPostRemote.subreddit,
        redditPostRemote.thumbnail,
        redditPostRemote.url
    )
}