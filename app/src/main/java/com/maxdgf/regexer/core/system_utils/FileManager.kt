package com.maxdgf.regexer.core.system_utils

import android.content.Context
import android.net.Uri
import java.io.IOException

class FileManager(private val context: Context) {
    fun openTextFile(uri: Uri): String {
        var fileContent = StringBuilder()

        val contentResolver = context.contentResolver

        try {
            val inputStream = contentResolver.openInputStream(uri)

            fileContent.append(
                inputStream?.use { stream ->
                    stream.readBytes().decodeToString()
                } ?: ""
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return fileContent.toString()
    }
}