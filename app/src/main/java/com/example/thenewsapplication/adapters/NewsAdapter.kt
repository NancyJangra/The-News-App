package com.example.thenewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewModel>() {

    inner class ArticleViewModel(itemView: View) : RecyclerView.ViewHolder(itemView)
    lateinit var articleImage: ImageView
    lateinit var articleSource: TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDateTime: TextView
    private val differCallback = object : DiffUtil.ItemCallback<Article>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewModel {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent, attachToRoot: false)
        )
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ArticleViewModel, position: Int) {
        TODO("Not yet implemented")
    }
}