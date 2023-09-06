package io.moonlighting.redditclientv2.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.moonlighting.redditclientv2.R


@Composable
fun ErrorMessage(
    error: String,
    modifier : Modifier = Modifier
) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = error)
    }
}

@Composable
fun LoadingScreen(
    modifier : Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(
            modifier = Modifier.size(size = 48.dp),
            color = Color.Red
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(text = stringResource(R.string.loading))
    }
}

@Composable
fun SubredditName(text: String, modifier: Modifier = Modifier, size: Int = 8) {
    Text(text, fontSize = size.sp, fontStyle = FontStyle.Italic, modifier = modifier)
}
@Composable
fun AuthorName(text: String, modifier: Modifier = Modifier, size: Int = 8) {
    Text(text, fontSize = size.sp, fontStyle = FontStyle.Italic, color = Color.Gray, modifier = modifier)
}