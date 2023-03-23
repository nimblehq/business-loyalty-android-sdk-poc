package co.nimblehq.loyalty.sdk.poc.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.toFormattedString(): String {
    val sdf = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
    return sdf.format(this)
}