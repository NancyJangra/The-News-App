package com.example.thenewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsprojectpractice.R

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewModel>() {

    inner class ArticleViewModel(itemView: View) : RecyclerView.ViewHolder(itemView)
    lateinit var articleImage: ImageView
    lateinit var articleSource: TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDateTime: TextView
    private val differCallback = object : DiffUtil.ItemCallback<Article>(){

    }
    val differ = AsyncListDiffer(adapter: this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewModel {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent, attachToRoot: false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((Article) -> Unit)? = null
    override fun onBindViewHolder(holder: ArticleViewModel, position: Int) {
        val article =  differ.currentList[position]
        articleImage = holder.itemView.findViewById<>(R.id.articleImage)

        articleSource.text = article.source?.name
        articleTitle.text = article.title
        articleDescription.text = article.description
        articleDateTime.text = article.publishedAt

        onItemClickListener?.let {
            it(article)
        }

    }
    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }
}