package com.maxdgf.regexer.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

import com.maxdgf.regexer.core.regex.RegexAnnotatedStringBuilder
import com.maxdgf.regexer.ui.data_management.data_models.RegexFlagItem
import com.maxdgf.regexer.ui.data_management.view_models.UiState

class TextFieldVisualTransformation(
    private val regexPattern: String,
    private val color: Color,
    private val flagsList: List<RegexFlagItem>,
    private val globalSearchState: Boolean,
    private val uiState: UiState
) : VisualTransformation {
    private val regexAnnotatedStringBuilder = RegexAnnotatedStringBuilder(uiState)

    override fun filter(text: AnnotatedString): TransformedText {
        uiState.updateRegexFlagsEnabledCount(flagsList.size) // updating enabled flags count

        val styledText = regexAnnotatedStringBuilder.setStyleOnMatchedWords(
            text,
            regexPattern,
            color,
            flagsList.map {
                flagItem -> flagItem.flag
            }.toSet(),
            globalSearchState
        )

        return TransformedText(
            styledText,
            OffsetMapping.Identity
        )
    }
}