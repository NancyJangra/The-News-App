package com.example.thenewsapp.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewModel>() {

    inner class ArticleViewModel(itemView: View) : RecyclerView.ViewHolder(itemView)
    lateinit var articleImage: ImageView
    lateinit var articleSource: TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDateTime: TextView
}