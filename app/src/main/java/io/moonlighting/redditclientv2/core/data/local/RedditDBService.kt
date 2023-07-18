package io.moonlighting.redditclientv2.core.data.local

import androidx.paging.PagingSource
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

    @Query("SELECT * FROM redditposts WHERE subreddit = :subreddit")
    fun redditPostsDBPaging(subreddit: String): PagingSource<Int, RedditPostEntity>

    @Query("DELETE FROM redditposts WHERE subreddit = :subreddit")
    fun removeAll(subreddit: String)

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
