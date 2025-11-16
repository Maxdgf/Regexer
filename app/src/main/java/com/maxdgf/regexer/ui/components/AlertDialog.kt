package com.maxdgf.regexer.ui.components

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**Creates and setups compose alert dialog with custom content.*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertUiDialog(
    state: Boolean,
    onDismissRequestFunction: () -> Unit,
    dialogContent: @Composable () -> Unit
) {
    if (state) {
        BasicAlertDialog(onDismissRequest = { onDismissRequestFunction() }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) { dialogContent() }
        }
    }
}