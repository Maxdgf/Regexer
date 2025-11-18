package com.maxdgf.regexer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**Creates and setups compose bottom sheet with custom content.*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomUiSheet(
    state: Boolean,
    skipPartiallyExpanded: Boolean,
    gesturesEnabled: Boolean = true,
    onDismissRequestFunction: () -> Unit,
    titleContent: @Composable () -> Unit = {},
    uiContent: @Composable () -> Unit
) {
    if (state) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = skipPartiallyExpanded
        )

        ModalBottomSheet(
            sheetState = sheetState,
            sheetGesturesEnabled = gesturesEnabled,
            onDismissRequest = { onDismissRequestFunction() }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                titleContent()
                uiContent()
            }
        }
    }
}