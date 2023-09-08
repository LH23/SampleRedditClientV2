package io.moonlighting.redditclientv2.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import io.moonlighting.redditclientv2.R

object RedditClientIcons {
    //
    val Play = Icons.Filled.PlayArrow

    val Pause: ImageVector
        @Composable
        get() = if (_pause != null) _pause!!
            else { _pause = ImageVector.vectorResource(id = com.google.android.exoplayer2.ui.R.drawable.exo_icon_pause)
                   _pause!!}
    private var _pause: ImageVector? = null
}