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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.0")

    defaultConfig {
        targetSdkVersion(30)
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

    buildFeatures {
        viewBinding = true
        renderScript = false
        aidl = false
        shaders = false
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

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
}

fun DependencyHandler.sharedTestImplementation(dependencyNotation: String) {
    testImplementation(dependencyNotation)
    androidTestImplementation(dependencyNotation)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.7")

    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0-rc01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta7")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")

    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    debugImplementation("androidx.fragment:fragment-testing:1.2.5")

    implementation("androidx.navigation:navigation-ui-ktx:2.2.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.2")

    implementation("androidx.room:room-ktx:2.2.5")
    implementation("androidx.room:room-runtime:2.2.5")
    kapt("androidx.room:room-compiler:2.2.5")

    implementation("androidx.paging:paging-runtime-ktx:2.1.2")

    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("dev.chrisbanes:insetter-ktx:0.3.0")
    implementation("net.objecthunter:exp4j:0.4.8")

    implementation("com.google.dagger:dagger:2.28.1")
    kapt("com.google.dagger:dagger-compiler:2.28.1")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.4")

    sharedTestImplementation("junit:junit:4.13")
    sharedTestImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.10.0")

    sharedTestImplementation("androidx.test:core-ktx:1.2.0")
    sharedTestImplementation("androidx.test:rules:1.2.0")
    sharedTestImplementation("androidx.test.ext:junit-ktx:1.1.1")
    sharedTestImplementation("androidx.arch.core:core-testing:2.1.0")

    testImplementation("org.robolectric:robolectric:4.3.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        allWarningsAsErrors = true
        jvmTarget = "1.8"

        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xallow-result-return-type",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
        )
    }
}
