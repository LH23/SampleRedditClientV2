package io.moonlighting.redditclientv2.core.data.local

import io.moonlighting.redditclientv2.core.data.local.model.RedditPostEntity
import io.moonlighting.redditclientv2.core.data.remote.model.RedditPostRemote
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RedditPostEntityTest {

    @Test
    fun `constructor from remote initializes properties correctly`() {
        // given
        val redditPostRemote = RedditPostRemote(
            fullname = "123fullname",
            title = "Title",
            author = "Author",
            subreddit = "Subreddit",
            thumbnail = "Thumbnail",
            url = "SourceURL"
        )

        // when
        val redditPostEntity = RedditPostEntity(redditPostRemote)

        // then Assert that the properties are initialized correctly
        assertEquals(redditPostRemote.fullname, redditPostEntity.fullname)
        assertEquals(redditPostRemote.title, redditPostEntity.title)
        assertEquals(redditPostRemote.author, redditPostEntity.author)
        assertEquals(redditPostRemote.subreddit, redditPostEntity.subreddit)
        assertEquals(redditPostRemote.thumbnail, redditPostEntity.thumbnail)
        assertEquals(redditPostRemote.url, redditPostEntity.sourceUrl)
    }
}