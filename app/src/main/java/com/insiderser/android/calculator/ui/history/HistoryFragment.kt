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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.insiderser.android.calculator.R
import com.insiderser.android.calculator.dagger.injector
import com.insiderser.android.calculator.databinding.HistoryFragmentBinding
import com.insiderser.android.calculator.ui.NavigationHost
import com.insiderser.android.calculator.utils.consume
import com.insiderser.android.calculator.utils.viewLifecycleScoped
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import javax.inject.Inject

class HistoryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HistoryViewModel by viewModels { viewModelFactory }

    private var binding: HistoryFragmentBinding by viewLifecycleScoped()

    private val historyAdapter: HistoryListAdapter by lazy { HistoryListAdapter() }

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        HistoryFragmentBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? NavigationHost)?.registerToolbarWithNavigation(binding.toolbar)
        binding.toolbar.setOnMenuItemClickListener { handleOnMenuItemClicked(it) }

        binding.appBar.applySystemWindowInsetsToPadding(top = true)

        with(binding.historyList) {
            setHasFixedSize(true)
            adapter = historyAdapter

            applySystemWindowInsetsToPadding(bottom = true)
        }

        viewModel.history.observe(viewLifecycleOwner) { historyAdapter.submitList(it) }
    }

    private fun handleOnMenuItemClicked(item: MenuItem) = consume {
        if (item.itemId == R.id.clear_all) {
            viewModel.clearHistory()
        }
    }
}
