package io.moonlighting.redditclientv2.core.data.remote.model

import io.moonlighting.redditclientv2.core.data.remote.RedditPostsJSONResponse

data class RedditPostRemote(val fullname: String,
                            val title: String,
                            val subreddit: String,
                            val author: String,
                            val thumbnail: String,
                            val url: String,
                            val createdUtc: Long,
) {

    constructor(jsonPost: RedditPostsJSONResponse.PostsJsonData.JsonPostData.JsonPost) :
            this(jsonPost.name,
                jsonPost.title,
                jsonPost.subredditNamePrefixed,
                jsonPost.author,
                jsonPost.thumbnail,
                jsonPost.url,
                jsonPost.createdUtc
            )
}