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

package com.insiderser.android.calculator.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.insiderser.android.calculator.dagger.injector
import com.insiderser.android.calculator.databinding.HistoryFragmentBinding
import com.insiderser.android.calculator.ui.FragmentWithViewBinding
import com.insiderser.android.calculator.ui.NavigationHost
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import javax.inject.Inject

class HistoryFragment : FragmentWithViewBinding<HistoryFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HistoryViewModel by viewModels { viewModelFactory }

    private val historyAdapter: HistoryListAdapter by lazy { HistoryListAdapter() }

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HistoryFragmentBinding = HistoryFragmentBinding.inflate(inflater)

    override fun onBindingCreated(binding: HistoryFragmentBinding, savedInstanceState: Bundle?) {
        (activity as? NavigationHost)?.registerToolbarWithNavigation(binding.toolbar)

        binding.appBar.doOnApplyWindowInsets { view, insets, initial ->
            view.updatePadding(top = initial.paddings.top + insets.systemWindowInsetTop)
        }

        with(binding.historyList) {
            setHasFixedSize(true)
            adapter = historyAdapter

            doOnApplyWindowInsets { view, insets, initial ->
                view.updatePadding(bottom = initial.paddings.bottom + insets.systemWindowInsetBottom)
            }
        }

        viewModel.history.observe(viewLifecycleOwner, historyAdapter::submitList)
    }
}
