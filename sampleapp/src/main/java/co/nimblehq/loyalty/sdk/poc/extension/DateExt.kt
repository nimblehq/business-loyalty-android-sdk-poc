package co.nimblehq.loyalty.sdk.poc.extension

import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TIME_PATTER_DDMMMYYY = "dd MMM yyyy"

fun Date.toFormattedString(pattern: String = DATE_TIME_PATTER_DDMMMYYY): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}
