package com.example.thenewsapplication.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thenewsapplication.models.Article
import com.example.thenewsapplication.models.NewsResponse
import com.example.thenewsapplication.repository.NewsRepository
import com.example.thenewsapplication.util.Resource
import kotlinx.coroutines.launch
import okhttp3.Response
import retrofit2.http.Query
import java.io.IOException
import java.util.Locale.IsoCountryCode

class NewsViewModel(app:Application, val newsRepository: NewsRepository): AndroidViewModel(app) {
    val headlines:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResponse: NewsResponse? = null
    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery : String? = null
    var oldSearchQuery : String? = null

    init {
        getHeadLines("us")
    }


    fun getHeadLines(countryCode: String)=viewModelScope.launch {
        headLinesInternet(countryCode)
    }

    fun searchNews(searchQuery: String)=viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }

    private fun handleHeadLinesResponse(response:Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                headlinesPage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldarticles = headlinesResponse?.articles
                    val newsArticles = resultResponse.articles
                    oldarticles?.addAll(newsArticles)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())

    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newsArticles = resultResponse.articles
                    oldArticles?.addAll(newsArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavourites(article: Article)=viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getFavouriteNews() = newsRepository.getFavouriteNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false

                }
            } ?: false
        }
    }

    private suspend fun headLinesInternet(countryCode: String){
        headlines.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())) {
                val response = newsRepository.getHeadlines(countryCode , headlinesPage)
                        headlines.postValue(handleHeadLinesResponse(response))
            } else{
                headlines.postValue(Resource.Error(message = "No Internet Connection"))
            }

        } catch (t: Throwable){
            when(t) {
                is IOException -> headlines.postValue(Resource.Error(message = "unable to connect"))
                else -> headlines.postValue(Resource.Error(message = "no signal"))

            }
        }

    }

    private suspend fun searchNewsInternet(searchQuery: String){
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())) {
                val response = newsRepository.searchNews(searchQuery , searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error(message =  "No internet Connection"))
            }
        }catch(t: Throwable){
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error(message = "Unable to connect"))
                else -> searchNews.postValue(Resource.Error(message = "No Signal"))
            }
        }

    }





}