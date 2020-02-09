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
package com.insiderser.android.calculator.calculator.dagger

import android.os.Bundle
import androidx.savedstate.SavedStateRegistryOwner
import com.insiderser.android.calculator.calculator.ui.CalculatorFragment
import com.insiderser.android.calculator.core.dagger.DefaultArgs
import com.insiderser.android.calculator.core.dagger.FeatureScope
import com.insiderser.android.calculator.core.dagger.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component

/**
 * Component for calculator module.
 */
@FeatureScope
@Component(modules = [ViewModelFactoryModule::class, CalculatorModule::class])
internal interface CalculatorComponent {

    /** Inject dependencies into [CalculatorFragment]. */
    fun inject(fragment: CalculatorFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance savedStateRegistryOwner: SavedStateRegistryOwner,
            @BindsInstance @DefaultArgs defaultArgs: Bundle? = null
        ): CalculatorComponent
    }
}
