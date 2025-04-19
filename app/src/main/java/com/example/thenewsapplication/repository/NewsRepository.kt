package com.example.thenewsapplication.repository

import com.example.thenewsapplication.api.RetrofitInstance
import com.example.thenewsapplication.db.ArticleDatabase
import com.example.thenewsapplication.models.Article
import java.util.Locale.IsoCountryCode

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(searchQuery, pageNumber)
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getFavouriteNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}