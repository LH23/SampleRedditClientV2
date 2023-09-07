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
            subredditName = "Subreddit",
            subredditId = "t5_asdf",
            author = "Author",
            thumbnail = "Thumbnail",
            url = "SourceURL",
            contentText = "",
            redditLink = "https://wwww.reddit.com/",
            postHint = "image",
            createdUtc = 0L
        )
        // when
        val redditPostEntity = RedditPostEntity(redditPostRemote)

        // then Assert that the properties are initialized correctly
        assertEquals(redditPostRemote.fullname, redditPostEntity.fullname)
        assertEquals(redditPostRemote.title, redditPostEntity.title)
        assertEquals(redditPostRemote.author, redditPostEntity.author)
        assertEquals(redditPostRemote.subredditName, redditPostEntity.subredditName)
        assertEquals(redditPostRemote.subredditId, redditPostEntity.subredditId)
        assertEquals(redditPostRemote.thumbnail, redditPostEntity.thumbnail)
        assertEquals(redditPostRemote.url, redditPostEntity.sourceUrl)
    }
}