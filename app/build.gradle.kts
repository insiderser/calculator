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

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        targetSdkVersion(29)
        minSdkVersion(23)

        versionName = "1.0.0"
        versionCode = 1
        applicationId = "com.insiderser.android.calculator"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.70")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.3")

    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")

    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.2.2")
    debugImplementation("androidx.fragment:fragment-testing:1.2.2")

    implementation("androidx.navigation:navigation-ui-ktx:2.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.1")

    implementation("androidx.room:room-ktx:2.2.4")
    implementation("androidx.room:room-runtime:2.2.4")
    kapt("androidx.room:room-compiler:2.2.4")

    implementation("androidx.paging:paging-runtime-ktx:2.1.1")

    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("dev.chrisbanes:insetter-ktx:0.2.1")
    implementation("net.objecthunter:exp4j:0.4.8")

    implementation("com.google.dagger:dagger:2.26")
    kapt("com.google.dagger:dagger-compiler:2.26")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.2")

    sharedTestImplementation("junit:junit:4.13")
    sharedTestImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.9.3")

    sharedTestImplementation("androidx.test:core:1.2.0")
    sharedTestImplementation("androidx.test:runner:1.2.0")
    sharedTestImplementation("androidx.test:rules:1.2.0")
    sharedTestImplementation("androidx.test.ext:junit:1.1.1")
    sharedTestImplementation("androidx.arch.core:core-testing:2.1.0")

    testImplementation("org.robolectric:robolectric:4.3.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
