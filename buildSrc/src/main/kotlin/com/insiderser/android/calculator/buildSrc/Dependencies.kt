/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.insiderser.android.calculator.buildSrc

/**
 * A wrapper for all dependencies' notations used in the project together with their versions.
 */
object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.6.0-rc03"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.2"

    const val exp4j = "net.objecthunter:exp4j:0.4.8"

    object Test {
        const val junit4 = "junit:junit:4.13"
        const val truth = "com.google.truth:truth:1.0.1"
        const val mockK = "io.mockk:mockk:1.9.3"

        object Robolectric {
            private const val version = "4.3.1"
            const val robolectric = "org.robolectric:robolectric:$version"
        }

        object AndroidX {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"
            const val ext = "androidx.test.ext:junit:1.1.1"
            const val extTruth = "androidx.test.ext:truth:1.2.0"
            const val arch = "androidx.arch.core:core-testing:2.1.0"

            object Espresso {
                private const val version = "3.2.0"
                const val core = "androidx.test.espresso:espresso-core:$version"
                const val intents = "androidx.test.espresso:espresso-intents:$version"
            }
        }
    }

    object Kotlin {
        private const val kotlinVersion = "1.3.61"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"

        object Coroutines {
            private const val version = "1.3.3"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.2.0"
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val material = "com.google.android.material:material:1.1.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"

        object Activity {
            private const val version = "1.1.0"
            const val activity = "androidx.activity:activity-ktx:$version"
        }

        object Fragment {
            private const val version = "1.2.1"
            const val fragment = "androidx.fragment:fragment-ktx:$version"
            const val testing = "androidx.fragment:fragment-testing:$version"
        }

        object Navigation {
            private const val version = "2.2.1"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Paging {
            private const val version = "2.1.1"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.2.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"
        }

        object Room {
            private const val version = "2.2.3"
            const val common = "androidx.room:room-common:$version"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val testing = "androidx.room:room-testing:$version"
        }
    }

    object Dagger {
        private const val version = "2.26"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object AssistedInject {
        private const val version = "0.5.2"
        const val annotations = "com.squareup.inject:assisted-inject-annotations-dagger2:$version"
        const val processor = "com.squareup.inject:assisted-inject-processor-dagger2:$version"
    }

    object Insetter {
        private const val version = "0.2.0"
        const val ktx = "dev.chrisbanes:insetter-ktx:$version"
    }
}
