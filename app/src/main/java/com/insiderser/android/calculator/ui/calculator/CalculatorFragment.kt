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
package com.insiderser.android.calculator.ui.calculator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.insiderser.android.calculator.R
import com.insiderser.android.calculator.dagger.injector
import com.insiderser.android.calculator.databinding.CalculatorFragmentBinding
import com.insiderser.android.calculator.ui.NavigationHost
import com.insiderser.android.calculator.utils.consume
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import javax.inject.Inject

/**
 * Main fragment that allows user to calculate mathematical expressions.
 */
class CalculatorFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CalculatorFragmentViewModel by viewModels { viewModelFactory }

    private lateinit var binding: CalculatorFragmentBinding

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        CalculatorFragmentBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? NavigationHost)?.registerToolbarWithNavigation(binding.toolbar)
        configureInsets()
        configureNumpad()
        observeData()
    }

    private fun configureInsets() {
        binding.toolbar.applySystemWindowInsetsToMargin(top = true)
        binding.numpad.root.applySystemWindowInsetsToPadding(bottom = true)
    }

    private fun configureNumpad() {
        (binding.numpad.root as ViewGroup).children
            .mapNotNull { it as? ViewGroup }
            .flatMap { it.children }
            .forEach { button ->
                button.setOnClickListener {
                    val tag = button.tag
                    if (tag is String) {
                        viewModel.onArithmeticButtonClicked(tag)
                    } else when (button.id) {
                        R.id.button_equal -> viewModel.onEqualButtonClicked()
                        R.id.button_backspace -> viewModel.onClearButtonClicked()
                    }
                }
            }

        binding.numpad.buttonBackspace.setOnLongClickListener {
            consume {
                viewModel.onClearButtonLongClick()
            }
        }
    }

    private fun observeData() {
        viewModel.expression.observe(viewLifecycleOwner) { expression ->
            binding.expression.run {
                text = expression
                post {
                    binding.expressionScrollView.smoothScrollTo(width, 0)
                }
            }
        }

        viewModel.result.observe(viewLifecycleOwner) { result ->
            binding.result.run {
                text = result
                post {
                    binding.resultScrollView.smoothScrollTo(width, 0)
                }
            }
        }
    }
}
