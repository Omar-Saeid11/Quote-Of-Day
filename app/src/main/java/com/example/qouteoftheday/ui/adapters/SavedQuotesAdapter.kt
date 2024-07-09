package com.example.qouteoftheday.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qouteoftheday.R
import com.example.qouteoftheday.databinding.QuoteItemBinding
import com.example.qouteoftheday.models.Quote
import com.example.qouteoftheday.util.ShareUtils

class SavedQuotesAdapter : RecyclerView.Adapter<SavedQuotesAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(val binding: QuoteItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun submitList(newList: List<Quote>) {
        differ.submitList(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = QuoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = differ.currentList[position]
        val binding = holder.binding

        binding.apply {
            rvQuoteTv.text = root.context.getString(R.string.quote, quote.quote)
            rvAuthorTv.text = root.context.getString(R.string.author, quote.author)

            rvQuoteTv.visibility = android.view.View.VISIBLE
            rvAuthorTv.visibility = android.view.View.VISIBLE

            rvQuoteLoading.visibility = android.view.View.GONE

            rvQuoteShare.setOnClickListener {
                rvQuoteShare.visibility = android.view.View.GONE
                ShareUtils.share(root, root.context)
                rvQuoteShare.visibility = android.view.View.VISIBLE
            }

            root.setOnLongClickListener {
                onItemLongClickListener?.invoke(quote) ?: false
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemLongClickListener: ((Quote) -> Boolean)? = null

    fun setOnItemLongClickListener(listener: (Quote) -> Boolean) {
        onItemLongClickListener = listener
    }
}
