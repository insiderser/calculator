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
package com.insiderser.android.calculator.dagger

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.insiderser.android.calculator.CalculatorApplication
import com.insiderser.android.calculator.ui.calculator.CalculatorFragment
import com.insiderser.android.calculator.ui.settings.SettingsFragment
import com.insiderser.android.calculator.ui.settings.theme.ThemeSettingDialogFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Main application-level dagger component that holds everything together.
 *
 * Use [DaggerAppComponent.factory] to create [AppComponent].
 *
 * Feature modules may create separate module components
 * that depend on one of [AppComponent]'s parent components.
 */
@Singleton
@Component(
    modules = [
        MainActivityModule::class,
        DataModule::class
    ]
)
interface AppComponent {

    fun inject(application: CalculatorApplication)
    fun inject(fragment: CalculatorFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: ThemeSettingDialogFragment)

    /**
     * Dagger factory for building [AppComponent], binding instances into a dagger graph.
     */
    @Component.Factory
    interface Factory {

        /**
         * Create [AppComponent] & bind [CalculatorApplication] into a dagger graph.
         */
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}

val Activity.injector: AppComponent get() = (application as CalculatorApplication).appComponent
val Fragment.injector: AppComponent get() = requireActivity().injector
