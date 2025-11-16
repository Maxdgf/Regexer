package com.maxdgf.regexer.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class CurrentThemeColor {
    @Composable
    fun getAdaptedCurrentThemeColor(
        useAlpha: Boolean,
        alphaFactor: Float = 1f
    ): Color {
        val adaptedColor = when (isSystemInDarkTheme()) {
            true -> Color.Companion.White
            false -> Color.Companion.Black
        }

        return when (useAlpha) {
            true -> adaptedColor.copy(alpha = alphaFactor)
            false -> adaptedColor
        }
    }
}