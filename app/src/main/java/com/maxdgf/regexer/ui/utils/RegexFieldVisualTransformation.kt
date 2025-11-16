package com.maxdgf.regexer.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

import com.maxdgf.regexer.core.regex.RegexpSyntaxAnnotatedStringBuilder

class RegexFieldVisualTransformation(
    private val exceptionMessage: String?,
    private val isLiteralFlagEnabled: Boolean
) : VisualTransformation {
    private val regexpSyntaxAnnotatedStringBuilder = RegexpSyntaxAnnotatedStringBuilder()

    override fun filter(text: AnnotatedString): TransformedText {
        val styledText = regexpSyntaxAnnotatedStringBuilder.setRegexpSyntaxStyleOnRegexStringPattern(
            text,
            exceptionMessage,
            isLiteralFlagEnabled
        )

        return TransformedText(
            styledText,
            OffsetMapping.Identity
        )
    }
}