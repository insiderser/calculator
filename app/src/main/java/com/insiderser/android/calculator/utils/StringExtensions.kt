package com.insiderser.android.calculator.utils

private val leadingZerosRegex = Regex("""\.?0+$""")

fun String.removeTrailingZeros(): String = replace(leadingZerosRegex, "")
