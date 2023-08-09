package io.moonlighting.redditclientv2.core.data.remote

import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote

class FakeRedditPostsRemoteDS(
    private val fakeData: List<RedditPostRemote>
): RedditPostsRemoteDS {

    override suspend fun getRedditTopPosts(
        subreddit: String,
        after: String?,
        before: String?,
        limit: Int
    ): List<RedditPostRemote> {
        return fakeData
    }

}
