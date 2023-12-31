package io.moonlighting.redditclientv2.app.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.moonlighting.redditclientv2.core.data.local.RedditDatabase
import io.moonlighting.redditclientv2.core.data.local.RedditPostsDao
import io.moonlighting.redditclientv2.core.data.remote.RedditApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object RedditAppModule {

    internal const val REDDIT_API_ENDPOINT = "https://www.reddit.com/"
    internal const val REDDIT_MOBILE_ENDPOINT = "https://m.reddit.com"

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideRedditApi(): RedditApiService {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(REDDIT_API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(RedditApiService::class.java)
    }

    @Provides
    fun provideRedditDB(
        @ApplicationContext context: Context
    ): RedditDatabase {
        return Room.databaseBuilder(
            context,
            RedditDatabase::class.java,
            "reddit-database"
        ).build()
    }

    @Provides
    fun provideRedditPostsDao(
        database: RedditDatabase
    ): RedditPostsDao {
        return database.redditPostsDao()
    }

}