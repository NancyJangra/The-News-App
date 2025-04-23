package com.example.thenewsapplication.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.thenewsapplication.R
import com.example.thenewsapplication.databinding.FragmentArticleBinding
import com.example.thenewsapplication.models.Article
import com.example.thenewsapplication.ui.NewsActivity
import com.example.thenewsapplication.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsViewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity as NewsActivity).viewModel
        val article = args.article

        // Initialize WebView
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            article.url?.let { url ->
                loadUrl(url)
            }
        }

        // Set up FAB click listener
        binding.fab.setOnClickListener {
            newsViewModel.saveArticle(article)
            Snackbar.make(view, "Article saved to favorites", Snackbar.LENGTH_SHORT).show()
        }
    }
}