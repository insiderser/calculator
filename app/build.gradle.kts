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

import com.insiderser.android.calculator.buildSrc.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        targetSdkVersion(29)
        minSdkVersion(23)

        versionName = "1.0.0"
        versionCode = 1
        applicationId = "com.insiderser.android.calculator"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArgument(
            "listener",
            "com.insiderser.android.calculator.test.listeners.CrashingRunListener"
        )

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    signingConfigs {
        named("debug") {
            storeFile = rootProject.file("debug.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }

    buildTypes {
        named("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    viewBinding {
        isEnabled = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("test") {
            java.srcDir("src/sharedTest/java")
        }
        named("androidTest") {
            java.srcDir("src/sharedTest/java")
        }
    }

    lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = true
        setLintConfig(rootProject.file("lint.xml"))
    }

    testOptions {
        animationsDisabled = true
        unitTests.run {
            isIncludeAndroidResources = true
        }
    }
}

fun DependencyHandler.sharedTestImplementation(dependencyNotation: String) {
    testImplementation(dependencyNotation)
    androidTestImplementation(dependencyNotation)
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(Libs.Kotlin.Coroutines.android)
    testImplementation(Libs.Kotlin.Coroutines.test)

    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.constraintLayout)

    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Lifecycle.lifecycleKtx)
    implementation(Libs.AndroidX.Lifecycle.liveDataKtx)
    implementation(Libs.AndroidX.Lifecycle.viewModelKtx)
    implementation(Libs.AndroidX.Lifecycle.savedState)

    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.Activity.activity)
    implementation(Libs.AndroidX.Fragment.fragment)
    debugImplementation(Libs.AndroidX.Fragment.testing)

    implementation(Libs.AndroidX.Navigation.ui)
    implementation(Libs.AndroidX.Navigation.fragment)

    implementation(Libs.AndroidX.Room.common)
    implementation(Libs.AndroidX.Room.ktx)
    implementation(Libs.AndroidX.Room.runtime)
    kapt(Libs.AndroidX.Room.compiler)

    implementation(Libs.AndroidX.Paging.runtime)

    implementation(Libs.timber)
    implementation(Libs.Insetter.ktx)
    implementation(Libs.exp4j)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)

    debugImplementation(Libs.leakCanary)

    sharedTestImplementation(Libs.Test.junit4)
    sharedTestImplementation(Libs.Test.truth)
    testImplementation(Libs.Test.mockK)

    sharedTestImplementation(Libs.Test.AndroidX.core)
    sharedTestImplementation(Libs.Test.AndroidX.runner)
    sharedTestImplementation(Libs.Test.AndroidX.rules)
    sharedTestImplementation(Libs.Test.AndroidX.ext)
    sharedTestImplementation(Libs.Test.AndroidX.arch)

    testImplementation(Libs.Test.Robolectric.robolectric)
    androidTestImplementation(Libs.Test.AndroidX.Espresso.core)
    androidTestImplementation(Libs.Test.AndroidX.Espresso.intents)
}
