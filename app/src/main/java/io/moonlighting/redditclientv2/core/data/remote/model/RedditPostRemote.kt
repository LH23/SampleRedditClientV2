package io.moonlighting.redditclientv2.core.data.remote.model

import io.moonlighting.redditclientv2.app.di.RedditAppModule
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsJSONResponse

data class RedditPostRemote(val fullname: String,
                            val title: String,
                            val subredditName: String,
                            val subredditId: String,
                            val author: String,
                            val thumbnail: String,
                            val url: String,
                            val fallbackUrl: String,
                            val contentText: String,
                            val redditLink: String,
                            val postHint: String?,
                            val createdUtc: Long,
) {
    constructor(jsonPost: RedditPostsJSONResponse.PostsJsonData.JsonPostData.JsonPost) :
            this(
                fullname = jsonPost.name,
                title = jsonPost.title,
                subredditName = jsonPost.subredditNamePrefixed,
                subredditId = jsonPost.subredditId,
                author = jsonPost.author,
                thumbnail = jsonPost.thumbnail,
                contentText = jsonPost.selftext,
                url = jsonPost.url,
                fallbackUrl = jsonPost.media?.redditVideo?.fallbackUrl!!,
                redditLink = RedditAppModule.REDDIT_MOBILE_ENDPOINT+jsonPost.permalink,
                postHint = jsonPost.postHint,
                createdUtc = jsonPost.createdUtc
            )
}