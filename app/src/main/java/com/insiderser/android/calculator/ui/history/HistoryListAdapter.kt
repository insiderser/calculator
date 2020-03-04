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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.insiderser.android.calculator.databinding.HistoryListItemBinding
import com.insiderser.android.calculator.model.HistoryItem

class HistoryListAdapter :
    PagedListAdapter<HistoryItem, HistoryViewHolder>(HistoryItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoryListItemBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HistoryViewHolder(
    private val binding: HistoryListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HistoryItem?) {
        binding.expression.text = item?.expression
        binding.result.text = item?.result
    }
}

private object HistoryItemDiffCallback : DiffUtil.ItemCallback<HistoryItem>() {

    override fun areItemsTheSame(
        oldItem: HistoryItem,
        newItem: HistoryItem
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: HistoryItem,
        newItem: HistoryItem
    ): Boolean = oldItem == newItem
}
