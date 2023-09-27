package io.moonlighting.redditclientv2.ui.compose.utils

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import io.moonlighting.redditclientv2.R
import io.moonlighting.redditclientv2.ui.theme.RedditClientIcons

@Composable
fun VideoPlayer(videoUri: Uri?, modifier: Modifier = Modifier) {
    if (videoUri == null) return
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            playWhenReady = true
            prepare()
        }
    }
    val playbackState = remember {
        exoPlayer.playbackState
    }
    val isPlaying = playbackState == Player.EVENT_IS_PLAYING_CHANGED // TODO check this

    Box(modifier = modifier) {
        AndroidView (
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        )
        IconButton(
            onClick = {
                if (isPlaying) exoPlayer.pause() else exoPlayer.play()
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = if (isPlaying) RedditClientIcons.Pause else RedditClientIcons.Play,
                contentDescription = if (isPlaying) stringResource(R.string.pause)
                                     else stringResource(R.string.play),
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}
