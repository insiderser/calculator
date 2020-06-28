package com.insiderser.android.calculator.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun removeTrailingZeros() {
        assertThat("233.000".removeTrailingZeros()).isEqualTo("233")
        assertThat("233.200".removeTrailingZeros()).isEqualTo("233.2")
        assertThat("233.245".removeTrailingZeros()).isEqualTo("233.245")
        assertThat("0.245".removeTrailingZeros()).isEqualTo("0.245")
        assertThat("0.20".removeTrailingZeros()).isEqualTo("0.2")
        assertThat("0.0".removeTrailingZeros()).isEqualTo("0")
        assertThat("133".removeTrailingZeros()).isEqualTo("133")
    }
}
