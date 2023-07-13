package io.moonlighting.redditclientv2.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import io.moonlighting.redditclientv2.core.data.remote.RedditPostRemote

@Dao
interface RedditPostsDao {
    @get:Query("SELECT * FROM redditposts")
    val redditPostsDB: List<RedditPostEntity>

    @Query("DELETE FROM redditposts")
    fun removeAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(entities: List<RedditPostEntity>)
}

@Database(entities = [RedditPostEntity::class], version = 1)
abstract class RedditDatabase : RoomDatabase() {

    abstract fun redditPostsDao(): RedditPostsDao
}

@Entity(tableName = "redditposts")
data class RedditPostEntity(
    @PrimaryKey
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
    val sourceUrl: String
) {
    constructor(redditPostRemote: RedditPostRemote) : this(
        redditPostRemote.fullname,
        redditPostRemote.title,
        redditPostRemote.author,
        redditPostRemote.subreddit,
        redditPostRemote.thumbnail,
        redditPostRemote.url
    )
}
