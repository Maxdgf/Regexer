package com.maxdgf.regexer.ui.data_management.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.maxdgf.regexer.ui.data_management.data_models.RegexFlagItem

class UiState : ViewModel() {
    //=============================================================== regexp settings and parameters management (states)
    var regexInputFieldState by mutableStateOf("")
    var currentRegexAsString by mutableStateOf("")
    var matchesCount by mutableIntStateOf(0)
    var regexExceptionMessage: String? by mutableStateOf(null)
    var regexExceptionView by mutableStateOf(false)
    var regexFlagsView by mutableStateOf(false)
    private val _regexFlagsList = MutableStateFlow(
        listOf<RegexFlagItem>(
            RegexFlagItem("Ignore Case", RegexOption.IGNORE_CASE),
            RegexFlagItem("Multiline", RegexOption.MULTILINE),
            RegexFlagItem("Literal", RegexOption.LITERAL),
            RegexFlagItem("Dot matches all", RegexOption.DOT_MATCHES_ALL),
            RegexFlagItem("Comments", RegexOption.COMMENTS),
            RegexFlagItem("Unix lines", RegexOption.UNIX_LINES)
        )
    )
    val regexFlagsList = _regexFlagsList.asStateFlow()
    private val _isRegexGlobalSearch = MutableStateFlow(true)
    val isRegexGlobalSearch = _isRegexGlobalSearch.asStateFlow()
    private val _isLiteralFlagEnabled = MutableStateFlow(false)
    val isLiteralFlagEnabled = _isLiteralFlagEnabled.asStateFlow()
    var regexFlagsEnabledCount by mutableIntStateOf(0)
    var regexSelectionMatchesColor: Color? by mutableStateOf(null)
    //=============================================================== regexp settings and parameters management (states)

    //============================================================== other (states)
    var textInputFieldState by mutableStateOf("")
    var bottomCheatSheetState by mutableStateOf(false)
    var dropdownMenuState by mutableStateOf(false)
    var colorPickerState by mutableStateOf(false)
    var aboutAppInfoSheetState by mutableStateOf(false)
    var isTestTextFieldFocusedState by mutableStateOf(false)

    var allSymbolsCount by mutableIntStateOf(0)
    var allWordsCount by mutableIntStateOf(0)
    var allStringsCount by mutableIntStateOf(0)
    //============================================================== other (states)

    //=============================================================== regexp settings and parameters management (state functions)
    fun updateRegexInputFieldState(text: String) {
        regexInputFieldState = text
        currentRegexAsString = text
    }
    fun updateMatchesCount(count: Int) { matchesCount = count }
    fun updateRegexExceptionMessageState(message: String?) { regexExceptionMessage = message }
    fun updateRegexExceptionViewState(state: Boolean) { regexExceptionView = state }
    fun updateRegexFlagsView(state: Boolean) { regexFlagsView = state }
    fun setSelectedRegexFlagState(
        state: Boolean,
        name: String
    ) {
        // get current flag item index by name
        val index = _regexFlagsList.value.indexOfFirst { flag ->
            name == flag.name
        }

        // update isSelected state in current flag item by index
        _regexFlagsList.update { list ->
            list.toMutableList().apply {
                this[index] = this[index].copy(isSelected = state)
            }
        }

        // if flag item name is Literal -> update isLiteralFlagEnabled state by current state
        if (name == "Literal") { _isLiteralFlagEnabled.value = state }
    }
    fun updateIsRegexGlobalSearch(state: Boolean) { _isRegexGlobalSearch.value = state }
    //=============================================================== regexp settings and parameters management (state functions)

    //============================================================== other (state functions)
    fun updateTextInputFieldState(text: String) { textInputFieldState = text }
    fun updateBottomCheatSheetState(state: Boolean) { bottomCheatSheetState = state }
    fun updateDropdownMenuState(state: Boolean) { dropdownMenuState = state }
    fun updateRegexFlagsEnabledCount(count: Int) { regexFlagsEnabledCount = count }
    fun updateRegexSelectionMatchesColorState(color: Color) { regexSelectionMatchesColor = color }
    fun updateColorPickerState(state: Boolean) { colorPickerState = state }
    fun updateAboutAppInfoSheetState(state: Boolean) { aboutAppInfoSheetState = state }
    fun updateIsTestTextFieldFocusedState(state: Boolean) { isTestTextFieldFocusedState = state }

    fun calculateAllSymbolsCount() { allSymbolsCount = textInputFieldState.length }
    fun calculateAllWordsCount() {
        val pattern = Regex("\\w+") //words
        val words = pattern.findAll(textInputFieldState)

        allWordsCount = words.count()
    }
    fun calculateAllStringsCount() {
        val pattern = Regex("\n") //strings(lines)
        val words = pattern.findAll(textInputFieldState)

        allStringsCount = words.count()
    }
    //============================================================== other (state functions)
}