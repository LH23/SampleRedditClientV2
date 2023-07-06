package io.moonlighting.redditclientv2

import android.app.Application
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryImpl
import io.moonlighting.redditclientv2.core.data.local.RedditDatabase
import io.moonlighting.redditclientv2.core.data.local.RedditPostsDao
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDSImpl
import io.moonlighting.redditclientv2.core.data.remote.RedditApiService
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDSImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val REDDIT_API_ENDPOINT = "https://www.reddit.com/"

class RedditClientApp: Application() {

    lateinit var repository: RedditClientRepository

    override fun onCreate() {
        super.onCreate()

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(REDDIT_API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val api = retrofit.create(RedditApiService::class.java)
        val db = databaseBuilder(this, RedditDatabase::class.java, "reddit-database").build()
        val localDS = RedditPostsLocalDSImpl(db.redditPostsDao())
        val remoteDS = RedditPostsRemoteDSImpl(api)
        repository = RedditClientRepositoryImpl(localDS, remoteDS)

    }
}
