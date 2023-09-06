package io.moonlighting.redditclientv2.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote

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
    @ColumnInfo(name = "subredditName")
    val subredditName: String,
    @ColumnInfo(name = "subredditId")
    val subredditId: String,
    @ColumnInfo(name = "subredditIcon")
    val subredditIcon: String? = null,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "contentText")
    val contentText: String,
    @ColumnInfo(name = "sourceUrl")
    val sourceUrl: String,
    @ColumnInfo(name = "redditLink")
    val redditLink: String,
    @ColumnInfo(name = "postHint")
    val postHint: String?,
    @ColumnInfo(name = "createdUtc")
    val createdUtc: Long,
    @ColumnInfo(name = "created_at")
    val createdLocallyAt: Long = System.currentTimeMillis()
) {
    constructor(redditPostRemote: RedditPostRemote) : this(
        gid = null,
        fullname = redditPostRemote.fullname,
        title = redditPostRemote.title,
        author = redditPostRemote.author,
        subredditName = redditPostRemote.subredditName,
        subredditId = redditPostRemote.subredditId,
        thumbnail = redditPostRemote.thumbnail,
        sourceUrl = redditPostRemote.url,
        contentText = redditPostRemote.contentText,
        redditLink = redditPostRemote.redditLink,
        postHint = redditPostRemote.postHint,
        createdUtc = redditPostRemote.createdUtc
    )
}