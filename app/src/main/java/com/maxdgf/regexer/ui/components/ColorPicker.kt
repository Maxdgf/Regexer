package com.maxdgf.regexer.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.maxdgf.regexer.R
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

/**Creates a color picker widget based on skydoves compose color picker library ->
https://github.com/skydoves/colorpicker-compose*/
@Composable
fun ColorPickerSheet(
    visibilityState: Boolean,
    initialColor: Color,
    configuration: Configuration,
    onDismissRequestFunction: () -> Unit,
    onColorChangedFunction: (color: Color) -> Unit
) {
    BottomUiSheet(
        state = visibilityState,
        skipPartiallyExpanded = true,
        onDismissRequestFunction = { onDismissRequestFunction() }
    ) {
        val controller = rememberColorPickerController() // color picker controller

        // vertical orientation
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(R.drawable.regexer_logo_mini),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                    )

                    Text(
                        text = "Select match color",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    IconButton(onClick = { onDismissRequestFunction() }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                            contentDescription = null
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AlphaTile(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        controller = controller
                    )
                }

                HsvColorPicker(
                    modifier = Modifier.weight(1f),
                    controller = controller,
                    onColorChanged = { onColorChangedFunction(it.color) },
                    initialColor = initialColor
                )

                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    controller = controller,
                    tileOddColor = Color.White,
                    tileEvenColor = Color.Black,
                    initialColor = initialColor
                )

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    controller = controller,
                    initialColor = initialColor
                )
            }
        } else { // horizontal orientation
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(R.drawable.regexer_logo_mini),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                    )

                    Text(
                        text = "Select match color",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    IconButton(onClick = { onDismissRequestFunction() }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                            contentDescription = null
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AlphaTile(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(5.dp)),
                            controller = controller
                        )

                        AlphaSlider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            controller = controller,
                            tileOddColor = Color.White,
                            tileEvenColor = Color.Black,
                            initialColor = initialColor
                        )

                        BrightnessSlider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            controller = controller,
                            initialColor = initialColor
                        )
                    }

                    HsvColorPicker(
                        modifier = Modifier.weight(1f),
                        controller = controller,
                        onColorChanged = { onColorChangedFunction(it.color) },
                        initialColor = initialColor
                    )
                }
            }
        }
    }
}