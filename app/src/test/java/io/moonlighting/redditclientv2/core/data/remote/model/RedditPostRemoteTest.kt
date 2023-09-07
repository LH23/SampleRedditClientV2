package io.moonlighting.redditclientv2.core.data.remote.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.moonlighting.redditclientv2.core.data.remote.RedditPostsJSONResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RedditPostRemoteTest {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Test
    fun `constructor from JSON response initializes properties correctly`() {
        // given
        val adapter: JsonAdapter<RedditPostsJSONResponse> =
            moshi.adapter(RedditPostsJSONResponse::class.java)

        val response: RedditPostsJSONResponse? = adapter.fromJson(redditJsonResponse)
        assertNotNull(response) // assert valid response parsing
        val redditJsonPost = response?.data?.postsData?.first()?.post
        assertNotNull(redditJsonPost) // assert valid json post

        // when
        val redditPostRemote = RedditPostRemote(redditJsonPost!!)

        // then Assert that the properties are initialized correctly
        assertEquals(redditPostRemote.fullname, redditJsonPost.name)
        assertEquals(redditPostRemote.title, redditJsonPost.title)
        assertEquals(redditPostRemote.author, redditJsonPost.author)
        assertEquals(redditPostRemote.subredditName, redditJsonPost.subredditNamePrefixed)
        assertEquals(redditPostRemote.thumbnail, redditJsonPost.thumbnail)
        assertEquals(redditPostRemote.url, redditJsonPost.url)
    }

    companion object {
        val redditJsonResponse = """
        {
          "kind": "Listing",
          "data": {
            "after": "t3_15m9i8s",
            "dist": 25,
            "modhash": "",
            "geo_filter": "",
            "children": [
              {
                "kind": "t3",
                "data": {
                  "approved_at_utc": null,
                  "subreddit": "MadeMeSmile",
                  "selftext": "",
                  "author_fullname": "t2_166mhe",
                  "saved": false,
                  "mod_reason_title": null,
                  "gilded": 1,
                  "clicked": false,
                  "title": "Grandparents knew they messed up 😭🤣😂🤣",
                  "subreddit_name_prefixed": "r/MadeMeSmile",
                  "hidden": false,
                  "pwls": 6,
                  "link_flair_css_class": "",
                  "downs": 0,
                  "thumbnail_height": 140,
                  "top_awarded_type": null,
                  "hide_score": false,
                  "name": "t3_15m1569",
                  "quarantine": false,
                  "link_flair_text_color": "dark",
                  "upvote_ratio": 0.93,
                  "author_flair_background_color": null,
                  "ups": 82344,
                  "total_awards_received": 8,
                  "media_embed": {},
                  "thumbnail_width": 140,
                  "author_flair_template_id": null,
                  "is_original_content": false,
                  "user_reports": [],
                  "secure_media": {
                    "reddit_video": {
                      "bitrate_kbps": 1200,
                      "fallback_url": "https://v.redd.it/we34ttmznzgb1/DASH_480.mp4?source=fallback",
                      "height": 854,
                      "width": 480,
                      "scrubber_media_url": "https://v.redd.it/we34ttmznzgb1/DASH_96.mp4",
                      "duration": 9,
                      "is_gif": false,
                      "transcoding_status": "completed"
                    }
                  },
                  "is_reddit_media_domain": true,
                  "is_meta": false,
                  "category": null,
                  "secure_media_embed": {},
                  "link_flair_text": ":snoo_putback: Good Vibes :snoo_tongue:",
                  "can_mod_post": false,
                  "score": 82344,
                  "approved_by": null,
                  "is_created_from_ads_ui": false,
                  "author_premium": true,
                  "thumbnail": "https://external-preview.redd.it/aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa.png?width=140&amp;height=140&amp;crop=140:140,smart&amp;format=jpg&amp;v=enabled&amp;lthumb=true&amp;s=efc6ca91c1a3fad96acd323cd4e9f0d8a19745f6",
                  "edited": false,
                  "author_flair_css_class": null,
                  "author_flair_richtext": [],
                  "gildings": {
                    "gid_2": 1,
                    "gid_3": 1
                  },
                  "post_hint": "hosted:video",
                  "content_categories": null,
                  "is_self": false,
                  "subreddit_type": "public",
                  "created": 1691545082,
                  "link_flair_type": "richtext",
                  "wls": 6,
                  "removed_by_category": null,
                  "banned_by": null,
                  "author_flair_type": "text",
                  "domain": "v.redd.it",
                  "allow_live_comments": true,
                  "selftext_html": null,
                  "likes": null,
                  "suggested_sort": null,
                  "banned_at_utc": null,
                  "url_overridden_by_dest": "https://v.redd.it/we34ttmznzgb1",
                  "view_count": null,
                  "archived": false,
                  "no_follow": false,
                  "is_crosspostable": false,
                  "pinned": false,
                  "over_18": false,
                  "preview": {
                    "images": [
                      {
                        "source": {
                          "url": "https://external-preview.redd.it/aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa.png?format=pjpg&amp;auto=webp&amp;s=bdb67b75aefd2f4a9511096f1853d36a2ee80b91",
                          "width": 1009,
                          "height": 1795
                        },
                        "resolutions": [
                          {
                            "url": "https://external-preview.redd.it/aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa.png?width=108&amp;crop=smart&amp;format=pjpg&amp;auto=webp&amp;s=e61d115cd1afb5693800d838c69ce0e9c9eed66c",
                            "width": 108,
                            "height": 192
                          },
                          {
                            "url": "https://external-preview.redd.it/aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa.png?width=216&amp;crop=smart&amp;format=pjpg&amp;auto=webp&amp;s=b0eb746ebee809a296fb6c99179e30ce830484af",
                            "width": 216,
                            "height": 384
                          },
                          {
                            "url": "https://external-preview.redd.it/aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa.png?width=320&amp;crop=smart&amp;format=pjpg&amp;auto=webp&amp;s=1ce06b94b492a402f0e12019afe575a783bfcca5",
                            "width": 320,
                            "height": 569
                          },
                          {
                            "url": "https://external-preview.redd.it/aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa.png?width=640&amp;crop=smart&amp;format=pjpg&amp;auto=webp&amp;s=1107bd42bdb60b948229ee8b28c552c3d6391a92",
                            "width": 640,
                            "height": 1138
                          },
                          {
                            "url": "https://external-preview.redd.it/aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa.png?width=960&amp;crop=smart&amp;format=pjpg&amp;auto=webp&amp;s=e09ab3959f08a1b6821fc78bb0619e9a95f00a11",
                            "width": 960,
                            "height": 1707
                          }
                        ],
                        "variants": {},
                        "id": "aXVmMG9wZHpuemdiMYfd5kEwLp5omqreNRS5dznq0FItOKRN-BgtWBBKqPYa"
                      }
                    ],
                    "enabled": false
                  },
                  "media_only": false,
                  "link_flair_template_id": "50fe798e-b971-11ea-8476-0e4d1ba3a22f",
                  "can_gild": false,
                  "spoiler": false,
                  "locked": false,
                  "author_flair_text": null,
                  "treatment_tags": [],
                  "visited": false,
                  "removed_by": null,
                  "mod_note": null,
                  "distinguished": null,
                  "subreddit_id": "t5_2uqcm",
                  "author_is_blocked": false,
                  "mod_reason_by": null,
                  "num_reports": null,
                  "removal_reason": null,
                  "link_flair_background_color": "#edeff1",
                  "id": "15m1569",
                  "is_robot_indexable": true,
                  "report_reasons": null,
                  "author": "SoCrazyItMustBeTrue",
                  "discussion_type": null,
                  "num_comments": 1487,
                  "send_replies": true,
                  "whitelist_status": "all_ads",
                  "contest_mode": false,
                  "mod_reports": [],
                  "author_patreon_flair": false,
                  "author_flair_text_color": null,
                  "permalink": "/r/MadeMeSmile/comments/15m1569/grandparents_knew_they_messed_up/",
                  "parent_whitelist_status": "all_ads",
                  "stickied": false,
                  "url": "https://v.redd.it/we34ttmznzgb1",
                  "subreddit_subscribers": 8125610,
                  "created_utc": 1691545082,
                  "num_crossposts": 15,
                  "media": {
                    "reddit_video": {
                      "bitrate_kbps": 1200,
                      "fallback_url": "https://v.redd.it/we34ttmznzgb1/DASH_480.mp4?source=fallback",
                      "height": 854,
                      "width": 480,
                      "scrubber_media_url": "https://v.redd.it/we34ttmznzgb1/DASH_96.mp4",
                      "duration": 9,
                      "is_gif": false,
                      "transcoding_status": "completed"
                    }
                  },
                  "is_video": true
                }
              }
            ],
            "before": null
          }
        }""".trimIndent()
    }
}