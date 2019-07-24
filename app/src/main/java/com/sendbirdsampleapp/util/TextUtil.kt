package com.sendbirdsampleapp.util

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan

object TextUtil {

    fun formatText(message: String): SpannableStringBuilder? {

        val regexBold = ".*\\*.*\\*.*".toRegex()
        val regexItalic = ".*\\_.*\\_.*".toRegex()

        val result = SpannableStringBuilder()
        var tmp: String
        val boldSpan = StyleSpan(android.graphics.Typeface.BOLD)
        val italicSpan = StyleSpan(android.graphics.Typeface.ITALIC)
        val boldItalicSpan = StyleSpan(android.graphics.Typeface.BOLD_ITALIC)

        for (word in message.split(" ")) {
            if (word.matches(regexBold) && word.matches(regexItalic)) {
                tmp = word.removePrefix("_*").removeSuffix("*_") + " "
                result.append(tmp, boldItalicSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else if (word.matches(regexBold)) {
                tmp = word.removePrefix("*").removeSuffix("*") + " "
                result.append(tmp, boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else if (word.matches(regexItalic)) {
                tmp = word.removePrefix("_").removeSuffix("_") + " "
                result.append(tmp, italicSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                tmp = "$word "
                result.append(tmp)
            }
        }

        return result
    }
}