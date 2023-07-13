package io.moonlighting.redditclientv2.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.moonlighting.redditclientv2.core.data.RedditClientRepository
import io.moonlighting.redditclientv2.core.data.RedditClientRepositoryImpl
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDS
import io.moonlighting.redditclientv2.core.data.local.RedditPostsLocalDSImpl
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDS
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsRemoteDSImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(
        redditClientRepositoryImpl: RedditClientRepositoryImpl
    ): RedditClientRepository

    @Binds
    abstract fun bindRemoteDS(
        redditPostsRemoteDSImpl: RedditPostsRemoteDSImpl
    ): RedditPostsRemoteDS

    @Binds
    abstract fun bindLocalDS(
        redditPostsLocalDSImpl: RedditPostsLocalDSImpl
    ): RedditPostsLocalDS

}