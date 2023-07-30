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

    @Query("SELECT * FROM redditposts ORDER BY _id ASC")
    fun redditPostsDBPaging(): PagingSource<Int, RedditPostEntity>

    @Query("DELETE FROM redditposts")
    suspend fun removeAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(entities: List<RedditPostEntity>)

    @Query("SELECT created_at FROM redditposts ORDER BY created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}

@Database(entities = [RedditPostEntity::class], version = 1)
abstract class RedditDatabase : RoomDatabase() {

    abstract fun redditPostsDao(): RedditPostsDao
}

@Entity(tableName = "redditposts")
data class RedditPostEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Int?, // to keep the list ordered
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
