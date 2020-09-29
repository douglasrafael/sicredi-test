package com.fsdevelopment.sicreditestapp.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts the date to a string.
 * The returned value will be based on the format parameter.
 */
fun Date.toString(format: String = "yyyy-MM-dd"): String = SimpleDateFormat(
    format, Locale.getDefault()
).apply { timeZone = TimeZone.getTimeZone("UTC") }.format(Date(this.time))