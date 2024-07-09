package com.example.qouteoftheday.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.qouteoftheday.R
import com.example.qouteoftheday.databinding.ActivityQuotesBinding
import com.example.qouteoftheday.ui.fragments.QuoteFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuotesBinding

    var atHome = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupTheme()
    }

    private fun setupNavigation() {
        binding.apply {
            myBookmarksImgBtn.setOnClickListener {
                val action = QuoteFragmentDirections.actionQuoteFragmentToBookmarkFragment()
                val navController = Navigation.findNavController(quotesNavHostFragment)
                navController.navigate(action)

                it.visibility = View.GONE
                backToQuotePage.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.myBookMarks)
                atHome = false
            }

            backToQuotePage.setOnClickListener {
                super.onBackPressed()

                it.visibility = View.GONE
                myBookmarksImgBtn.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.app_name)
                atHome = true
            }
        }
    }

    private fun setupTheme() {
        setTheme(R.style.Base_Theme_QouteOfTheDay)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!atHome) {
            binding.backToQuotePage.visibility = View.GONE
            binding.myBookmarksImgBtn.visibility = View.VISIBLE
            binding.activityTitle.text = resources.getText(R.string.app_name)
            atHome = true
        }
    }
}

