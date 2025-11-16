package com.maxdgf.regexer.ui.data_management.data_models

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class RegexFlagItem(
    val name: String,
    val flag: RegexOption,
    val isSelected: Boolean = false,
    val id: String = UUID.randomUUID().toString() // for lazy column item key
)
