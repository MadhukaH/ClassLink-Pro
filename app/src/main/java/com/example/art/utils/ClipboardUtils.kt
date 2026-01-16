package com.example.art.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Utility functions for clipboard operations
 */
object ClipboardUtils {
    
    /**
     * Copy text to system clipboard
     * @param context Android context
     * @param text Text to copy
     * @param label Label for clipboard entry
     * @return true if successful, false otherwise
     */
    fun copyToClipboard(context: Context, text: String, label: String = "Zoom Class Message"): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard?.setPrimaryClip(clip)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
