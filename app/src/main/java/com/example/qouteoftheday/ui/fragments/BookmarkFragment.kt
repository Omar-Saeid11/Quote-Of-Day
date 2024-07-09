package com.example.qouteoftheday.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qouteoftheday.R
import com.example.qouteoftheday.databinding.FragmentBookmarksBinding
import com.example.qouteoftheday.ui.adapters.SavedQuotesAdapter
import com.example.qouteoftheday.viewmodels.QuoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(R.layout.fragment_bookmarks) {

    private lateinit var binding: FragmentBookmarksBinding
    private val viewModel by activityViewModels<QuoteViewModel>()
    private lateinit var savedQuotesAdapter: SavedQuotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookmarksBinding.bind(view)

        setupRecyclerView()

        savedQuotesAdapter.setOnItemLongClickListener { quote ->
            val clipBoardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clipdata = ClipData.newPlainText(
                "quote",
                "\"${quote.quote}\"\n\n- ${quote.author}"
            )

            clipBoardManager.setPrimaryClip(clipdata)

            Snackbar.make(view, "Quote Copied!", Snackbar.LENGTH_SHORT).show()
            true
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val quote = savedQuotesAdapter.differ.currentList[position]
                viewModel.deleteQuote(quote)
                Snackbar.make(view, "Removed Bookmark!", Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction("Undo") {
                            viewModel.saveQuote(quote)
                            Snackbar.make(view, "Re-saved!", Snackbar.LENGTH_SHORT).show()
                        }
                        setActionTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.light_blue
                            )
                        )
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedQuotes)
        }

        viewModel.getSavedQuotes().observe(viewLifecycleOwner) { articles ->
            savedQuotesAdapter.submitList(articles)

            if (articles.isEmpty()) {
                binding.rvSavedQuotes.visibility = View.GONE
                binding.tvNoBookmarks.visibility = View.VISIBLE
            } else {
                binding.rvSavedQuotes.visibility = View.VISIBLE
                binding.tvNoBookmarks.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        savedQuotesAdapter = SavedQuotesAdapter()
        binding.rvSavedQuotes.apply {
            adapter = savedQuotesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
