package com.maxdgf.regexer.core.system_utils

import android.content.Context
import android.widget.Toast

class Toaster(private val context: Context) {

    /**Shows a toast message (long or short by time).*/
    fun showToast(
        message: String,
        isLong: Boolean = false
    ) {
        // show toast message (long or short by time)
        when (isLong) {
            true -> {
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }
            false -> {
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}