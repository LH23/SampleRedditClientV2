package io.moonlighting.redditclientv2.core.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RedditPostsDao {

    @Query("SELECT * FROM redditposts ORDER BY gid ASC")
    fun redditPostsDBPaging(): PagingSource<Int, RedditPostEntity>

    @Query("DELETE FROM redditposts")
    suspend fun removeAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(entities: List<RedditPostEntity>)

    @Query("SELECT created_at FROM redditposts ORDER BY created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?

    @Query("SELECT * FROM redditposts WHERE fullname= :fullname")
    fun redditPost(fullname: String): Flow<RedditPostEntity>
}

@Database(entities = [RedditPostEntity::class], version = 1)
abstract class RedditDatabase : RoomDatabase() {

    abstract fun redditPostsDao(): RedditPostsDao
}

