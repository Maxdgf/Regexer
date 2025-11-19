package com.maxdgf.regexer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maxdgf.regexer.R

/**Creates a title with app mini logo, text and (optional) dismiss button.*/
@Composable
fun RegexerUiDialogTitle(
    titleText: String,
    modifier: Modifier = Modifier,
    spawnDismissDialogButton: Boolean,
    spawnDismissDialogButtonFunction: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.regexer_logo_mini),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
        )

        Text(
            text = titleText,
            modifier = Modifier
                .weight(1f)
                .basicMarquee(Int.MAX_VALUE),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        if (spawnDismissDialogButton) {
            IconButton(onClick = { spawnDismissDialogButtonFunction() }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = null
                )
            }
        }
    }
}