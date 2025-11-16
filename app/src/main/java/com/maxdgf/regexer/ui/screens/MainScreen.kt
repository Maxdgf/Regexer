package com.maxdgf.regexer.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

import com.maxdgf.regexer.OPEN_SOURCE_PROJECT_DESCRIPTION
import com.maxdgf.regexer.R
import com.maxdgf.regexer.REGEXER_APP_GITHUB_REPO_LINK
import com.maxdgf.regexer.REGEXER_APP_INFO
import com.maxdgf.regexer.REGEX_CHEAT_SHEET_TEXT
import com.maxdgf.regexer.core.regex.RegexpSyntaxAnnotatedStringBuilder
import com.maxdgf.regexer.core.system_utils.AppManager
import com.maxdgf.regexer.core.system_utils.ClipBoardManager
import com.maxdgf.regexer.core.system_utils.Toaster
import com.maxdgf.regexer.core.system_utils.UrlOpener
import com.maxdgf.regexer.currentSelectionMatchesColor
import com.maxdgf.regexer.dataStore
import com.maxdgf.regexer.ui.components.AlertUiDialog
import com.maxdgf.regexer.ui.components.BottomUiSheet
import com.maxdgf.regexer.ui.components.ColorPickerSheet
import com.maxdgf.regexer.ui.data_management.view_models.UiState
import com.maxdgf.regexer.ui.utils.CurrentThemeColor
import com.maxdgf.regexer.ui.utils.RegexFieldVisualTransformation
import com.maxdgf.regexer.ui.utils.TextFieldVisualTransformation

// Main App Screen Composable function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(uiState: UiState = viewModel()) {
    val defaultSelectionColor = remember { Color.Yellow } // default match selection color

    val haptic = LocalHapticFeedback.current // get haptic
    val activity = LocalActivity.current // get activity
    val context = LocalContext.current // get context
    val configuration = LocalConfiguration.current // get screen configuration
    val dataStore = context.dataStore // get app datastore

    //========================================================================================= classes init
    val currentThemeColor = remember { CurrentThemeColor() }
    val appManager = remember { AppManager(activity, context) }
    val clipBoardManager = remember { ClipBoardManager(context) }
    val regexpSyntaxAnnotatedStringBuilder = remember { RegexpSyntaxAnnotatedStringBuilder() }
    val urlOpener = remember { UrlOpener(context) }
    val toaster = remember { Toaster(context) }
    //========================================================================================= classes init

    //========================================================================================= viewmodel state value observers
    val regexFlagsList by uiState.regexFlagsList.collectAsState()
    val isGlobalSearchState by uiState.isRegexGlobalSearch.collectAsState()
    val isLiteralFlagEnabledState by uiState.isLiteralFlagEnabled.collectAsState()
    //========================================================================================= viewmodel state value observers

    //================================================================================= asynchronous retrieve the current match highlight color from the datastore and set it to the viewmodel state variable
    LaunchedEffect(Unit) {
        val currentRegexMatchesSelectionColor = dataStore.data.map {
            it[currentSelectionMatchesColor]
        }

        currentRegexMatchesSelectionColor.collect { colorLong ->
            colorLong?.let {
                val color = Color.fromColorLong(it)
                uiState.updateRegexSelectionMatchesColorState(color)
            } ?: uiState.updateRegexSelectionMatchesColorState(defaultSelectionColor)
        }
    }
    //================================================================================= asynchronous retrieve the current match highlight color from the datastore and set it to the viewmodel state variable

    //================================================================================= asynchronous setting the color in the datastore parameter when the state of the match highlight color changes
    LaunchedEffect(uiState.regexSelectionMatchesColor) {
        uiState.regexSelectionMatchesColor?.let { color ->
            dataStore.edit { // edit datastore
                it[currentSelectionMatchesColor] = color.value.toLong()
            }
        }

        delay(10) //delay
    }
    //================================================================================= asynchronous setting the color in the datastore parameter when the state of the match highlight color changes

    //================================================================================= asynchronous calculation of test text characteristics
    LaunchedEffect(uiState.textInputFieldState) {
        uiState.calculateAllSymbolsCount() // calculate all symbols count in test text
        uiState.calculateAllWordsCount() // calculate all words count in test text
        uiState.calculateAllStringsCount() // calculate all strings count in test text

        delay(10) // update delay
    }
    //================================================================================= asynchronous calculation of test text characteristics

    Scaffold(
        topBar = { // top app bar
            TopAppBar(
                navigationIcon = {
                    Image(
                        bitmap = ImageBitmap.imageResource(R.drawable.regexer_logo_mini),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(25.dp)
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic
                            uiState.updateBottomCheatSheetState(true)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_book_24),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic
                        uiState.updateDropdownMenuState(true)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_menu_24),
                            contentDescription = null
                        )
                    }

                    Box {
                        DropdownMenu(
                            expanded = uiState.dropdownMenuState,
                            onDismissRequest = { uiState.updateDropdownMenuState(false) }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    uiState.updateAboutAppInfoSheetState(true)
                                    uiState.updateDropdownMenuState(false)
                                },
                                text = {
                                    Row {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_info_24),
                                            contentDescription = null,
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        )

                                        Text(
                                            text = "About app",
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }
                            )

                            HorizontalDivider()

                            DropdownMenuItem(
                                onClick = { appManager.breakApp() },
                                text = {
                                    Row {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_exit_to_app_24),
                                            contentDescription = null,
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        )

                                        Text(
                                            text = "Exit",
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        //============================================================================== Regex exception view alert dialog
        AlertUiDialog(
            state = uiState.regexExceptionView,
            onDismissRequestFunction = { uiState.updateRegexExceptionViewState(false) }
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(R.drawable.outline_error_24),
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(
                        text = "Error view",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { uiState.updateRegexExceptionViewState(false) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_clear_24),
                            contentDescription = null
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.Red,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .height(200.dp)
                ) {
                    val verticalScroll = rememberScrollState()
                    val horizontalScroll = rememberScrollState()

                    Text(
                        text = uiState.regexExceptionMessage ?: "",
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                            .verticalScroll(verticalScroll)
                            .horizontalScroll(horizontalScroll)
                    )
                }
            }
        }
        //============================================================================== Regex exception view alert dialog

        //============================================================================== Regex flags selection dialog
        AlertUiDialog(
            state = uiState.regexFlagsView,
            onDismissRequestFunction = { uiState.updateRegexFlagsView(false) }
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) { Text(text = uiState.regexFlagsEnabledCount.toString()) }
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_filter_list_24),
                            contentDescription = null
                        )
                    }

                    Text(
                        text = "Flags and other",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { uiState.updateRegexFlagsView(false) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_clear_24),
                            contentDescription = null
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .height(200.dp)
                ) {
                    // regexp flags list view
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    ) {
                        items(
                            items = regexFlagsList,
                            key = { flag -> flag.id }
                        ) { flag ->
                            Row {
                                Text(
                                    text = flag.name,
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically)
                                )
                                Checkbox(
                                    checked = flag.isSelected,
                                    onCheckedChange = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic
                                        uiState.setSelectedRegexFlagState(
                                            when (flag.isSelected) {
                                                true -> false
                                                false -> true
                                            },
                                            flag.name
                                        )
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                Row {
                    Text(
                        text = "🌐 Global search",
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = isGlobalSearchState,
                        onCheckedChange = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic
                            uiState.updateIsRegexGlobalSearch(
                                when (isGlobalSearchState) {
                                    true -> false
                                    false -> true
                                }
                            )
                        }
                    )
                }
            }
        }
        //============================================================================== Regex flags selection dialog

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val dimThemeColor = currentThemeColor.getAdaptedCurrentThemeColor(true, alphaFactor = 0.5f)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                uiState.regexExceptionMessage?.let { // if regex exception message is not null, show error view button
                    Box(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Red,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .height(58.dp)
                            .padding(10.dp)
                            .clickable(onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic
                                uiState.updateRegexExceptionViewState(true) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_error_24),
                            contentDescription = null,
                            tint = Color.Red,
                        )
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = uiState.regexInputFieldState,
                    onValueChange = { newValue -> uiState.updateRegexInputFieldState(newValue) },
                    leadingIcon = {
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic
                            uiState.updateRegexFlagsView(true)
                        }) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) { Text(text = uiState.regexFlagsEnabledCount.toString()) }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_filter_list_24),
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic

                            if (uiState.regexInputFieldState.isNotEmpty()) {
                                uiState.updateRegexInputFieldState("")
                                toaster.showToast("regexp cleared!") // toast message
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_clear_24),
                                contentDescription = null
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "enter your regexp pattern...",
                            color = dimThemeColor,
                            modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                        )
                    },
                    singleLine = true,
                    visualTransformation = RegexFieldVisualTransformation(
                        uiState.regexExceptionMessage,
                        isLiteralFlagEnabledState
                    ), //test
                    colors = OutlinedTextFieldDefaults.colors(
                        // if regex exception message not null -> set red colors
                        focusedBorderColor = uiState.regexExceptionMessage?.let {
                            Color.Red
                        } ?: MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = uiState.regexExceptionMessage?.let {
                            MaterialTheme.colorScheme.error
                        } ?: MaterialTheme.colorScheme.onSecondary
                    )
                )

                // regexp matches view
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterVertically)
                        .height(58.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.outline_check_circle_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = uiState.matchesCount.toString(), // all matches count now
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = uiState.isTestTextFieldFocusedState) {
                // text data and actions view
                Box(
                    modifier = Modifier
                        .padding(
                            top = 2.dp,
                            bottom = 2.dp,
                            start = 10.dp,
                            end = 10.dp
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        // text data view
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "\uD83D\uDD21: ${uiState.allSymbolsCount}", // all symbols count
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "\uD83D\uDD24: ${uiState.allWordsCount}", // all words count
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "↪\uFE0F: ${uiState.allStringsCount}", // all strings count
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            // clear all text from text field icon button
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic

                                    if (uiState.textInputFieldState.isNotEmpty()) {
                                        uiState.updateTextInputFieldState("") // clear all text from text field state
                                        toaster.showToast("test text cleared!")
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_clear_24),
                                    contentDescription = null
                                )
                            }

                            // paste text from clipboard icon button
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress) //haptic
                                    val clipData =
                                        clipBoardManager.getClipboardText() // get data from clipboard

                                    if (clipData.isNotEmpty()) {
                                        uiState.updateTextInputFieldState(clipData) // set data to text field state
                                        toaster.showToast("text pasted!")
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_content_paste_24),
                                    contentDescription = null
                                )
                            }

                            // colorpicker view icon button
                            IconButton(onClick = { uiState.updateColorPickerState(true) }) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            uiState.regexSelectionMatchesColor ?: Color.Transparent
                                        ) // background - current matches count
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_draw_24),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .onFocusChanged{ uiState.updateIsTestTextFieldFocusedState(it.isFocused) },
                value = uiState.textInputFieldState,
                onValueChange = { newValue -> uiState.updateTextInputFieldState(newValue) },
                placeholder = {
                    Text(
                        text = "enter your test text...",
                        color = dimThemeColor,
                        modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                    )
                },
                visualTransformation = TextFieldVisualTransformation(
                    uiState.currentRegexAsString,
                    uiState.regexSelectionMatchesColor ?: defaultSelectionColor,
                    regexFlagsList.filter { flag -> flag.isSelected },
                    isGlobalSearchState,
                    uiState
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }

        //============================================================================== Regex cheat sheet
        BottomUiSheet(
            state = uiState.bottomCheatSheetState,
            skipPartiallyExpanded = true,
            onDismissRequestFunction = { uiState.updateBottomCheatSheetState(false) },
            modifier = Modifier.fillMaxHeight(),
            gesturesEnabled = false
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
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
                        text = "Regex mini-cheat sheet",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    IconButton(onClick = { uiState.updateBottomCheatSheetState(false) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                            contentDescription = null
                        )
                    }
                }

                val verticalScroll = rememberScrollState()

                Text(
                    text = regexpSyntaxAnnotatedStringBuilder.setRegexpSyntaxStyleOnRegexStringPattern(
                        buildAnnotatedString {
                            append(REGEX_CHEAT_SHEET_TEXT)
                        },
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .verticalScroll(verticalScroll)
                )
            }
        }
    }
    //============================================================================== Regex cheat sheet

    //============================================================================== About Regexer APP sheet
    BottomUiSheet(
        state = uiState.aboutAppInfoSheetState,
        skipPartiallyExpanded = true,
        onDismissRequestFunction = { uiState.updateBottomCheatSheetState(false) },
        modifier = Modifier.fillMaxHeight(),
        gesturesEnabled = false
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
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
                    text = "About ${stringResource(R.string.app_name)} App",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                IconButton(onClick = { uiState.updateAboutAppInfoSheetState(false) }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                        contentDescription = null
                    )
                }
            }

            // project info
            Text(
                text = REGEXER_APP_INFO,
                modifier = Modifier.fillMaxWidth()
            )

            // open source project description
            Text(
                text = OPEN_SOURCE_PROJECT_DESCRIPTION,
                modifier = Modifier.fillMaxWidth()
            )

            // project repo clickable text link
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color.Cyan,
                            textDecoration = TextDecoration.Underline
                        )
                    ) { append(REGEXER_APP_GITHUB_REPO_LINK) }
                },
                modifier = Modifier.clickable(
                    onClick = {
                        urlOpener.openUrl(REGEXER_APP_GITHUB_REPO_LINK)
                    }
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = appManager.getAppVersionName()?.let {
                    "version: $it"
                } ?: "",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
    //============================================================================== About Regexer APP sheet

    //============================================================================== Color picker sheet
    ColorPickerSheet(
        visibilityState = uiState.colorPickerState,
        onDismissRequestFunction = { uiState.updateColorPickerState(false) },
        onColorChangedFunction = uiState::updateRegexSelectionMatchesColorState,
        initialColor = uiState.regexSelectionMatchesColor ?: defaultSelectionColor,
        configuration = configuration
    )
    //============================================================================== Color picker sheet
}