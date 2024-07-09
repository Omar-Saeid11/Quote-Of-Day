package com.example.qouteoftheday.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.qouteoftheday.R
import com.example.qouteoftheday.databinding.FragmentQuoteBinding
import com.example.qouteoftheday.models.Quote
import com.example.qouteoftheday.ui.QuotesActivity
import com.example.qouteoftheday.util.Resource
import com.example.qouteoftheday.util.ShareUtils
import com.example.qouteoftheday.viewmodels.QuoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

@AndroidEntryPoint
class QuoteFragment : Fragment(R.layout.fragment_quote) {

    private var _binding: FragmentQuoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<QuoteViewModel>()
    private var quote: Quote? = null
    private var quoteShown = false
    private var isBookMarked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuoteBinding.bind(view)

        setupObservers()
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        viewModel.quote.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                    hideTextViews()
                    binding.apply {
                        noQuote.visibility = View.GONE
                        fab.visibility = View.GONE
                        quoteShare.visibility = View.GONE
                    }
                    quoteShown = false
                    quote = null
                }

                is Resource.Success -> {
                    hideProgressBar()
                    showTextViews()
                    binding.apply {
                        noQuote.visibility = View.GONE
                        fab.visibility = View.VISIBLE
                        quoteShare.visibility = View.VISIBLE
                        response.data?.let { quoteResponse ->
                            quote = quoteResponse
                            quoteTv.text = resources.getString(R.string.quote, quoteResponse.quote)
                            authorTv.text =
                                resources.getString(R.string.author, quoteResponse.author)
                        }
                    }
                    quoteShown = true
                }

                is Resource.Error -> {
                    hideProgressBar()
                    hideTextViews()
                    binding.apply {
                        noQuote.visibility = View.VISIBLE
                        fab.visibility = View.GONE
                        quoteShare.visibility = View.GONE
                        response.message?.let {
                            noQuote.text = it
                        }
                    }
                    quoteShown = false
                    quote = null
                }
            }
        }

        viewModel.bookmarked.observe(viewLifecycleOwner) { bookmarked ->
            isBookMarked = bookmarked
            binding.fab.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (isBookMarked) R.drawable.ic_bookmarked else R.drawable.ic_unbookmarked
                )
            )
        }
    }

    private fun setupClickListeners() {
        binding.quoteCard.setOnTouchListener { v, event ->
            val displayMetrics = resources.displayMetrics
            val cardWidth = binding.quoteCard.width
            val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)

            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    var currentX = binding.quoteCard.x
                    binding.quoteCard.animate()
                        .x(cardStart)
                        .setDuration(150)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                                    delay(100)
                                    if (currentX < MIN_SWIPE_DISTANCE) {
                                        viewModel.getRandomQuote()
                                        currentX = 0f
                                    }
                                }
                            }
                        }).start()
                    binding.extraText.text = getString(R.string.info)
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX
                    if (newX - cardWidth < cardStart) {
                        binding.quoteCard.animate()
                            .x(min(cardStart, newX - (cardWidth / 2)))
                            .setDuration(0)
                            .start()
                        binding.extraText.text =
                            if (binding.quoteCard.x < MIN_SWIPE_DISTANCE) getString(R.string.release) else getString(
                                R.string.info
                            )
                    }
                }
            }
            v.performClick()
            true
        }

        binding.fab.setOnClickListener {
            if (isBookMarked) {
                viewModel.deleteQuote(quote!!)
                if ((activity as QuotesActivity).atHome) {
                    Snackbar.make(
                        requireActivity().findViewById(R.id.quotesNavHostFragment),
                        "Removed Bookmark!",
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        setAction("Undo") {
                            viewModel.saveQuote(quote!!)
                            if ((activity as QuotesActivity).atHome) {
                                makeSnackBar(view, "Re-saved!")
                            }
                            isBookMarked = !isBookMarked
                        }
                        setActionTextColor(ContextCompat.getColor(view.context, R.color.light_blue))
                        show()
                    }
                }
                binding.fab.visibility = View.INVISIBLE
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                    delay(3000)
                    withContext(Dispatchers.Main) {
                        binding.fab.visibility = View.VISIBLE
                    }
                }
            } else {
                viewModel.saveQuote(quote!!)
                view?.let { it1 -> makeSnackBar(it1, "Successfully saved Quote!") }
            }
        }

        binding.quoteShare.setOnClickListener {
            binding.quoteShare.visibility = View.GONE
            ShareUtils.share(binding.cardHolder, activity as QuotesActivity)
            binding.quoteShare.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar() {
        binding.quoteLoading.visibility = View.VISIBLE
        hideTextViews()
    }

    private fun showTextViews() {
        binding.quoteTv.visibility = View.VISIBLE
        binding.authorTvShareBtnParent.visibility = View.VISIBLE
    }

    private fun hideTextViews() {
        binding.quoteTv.visibility = View.GONE
        binding.authorTvShareBtnParent.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.quoteLoading.visibility = View.GONE
    }

    private fun makeSnackBar(view: View, message: String) {
        if ((activity as QuotesActivity).atHome) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
            binding.fab.visibility = View.INVISIBLE
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                delay(3000)
                withContext(Dispatchers.Main) {
                    binding.fab.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        private const val MIN_SWIPE_DISTANCE = 150
    }
}
