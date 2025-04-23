package com.example.thenewsapplication.ui.fragments

import android.content.Context
import android.icu.text.Transliterator.Position
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newsprojectpractice.databinding.FragmentFavouritesBinding
import com.example.thenewsapp.adapters.NewsAdapter
import com.example.thenewsapplication.R
import com.example.thenewsapplication.ui.NewsActivity
import com.example.thenewsapplication.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class FavouritesFragment : Fragment() {
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavouritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupFavouritesRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_favouritesFragment_to_articleFragment, bundle)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
        return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder){
            val position = ViewHolder.adapterPosition
            val article = NewsAdapter.differ.currentList[Position]
            newsViewModel.deleteArticle(article)
            Snackbar.make(view,"Removed from favourites", Snackbar.LENGTH_LONG).apply {
                setAction("Undo"){
                    newsViewModel.addToFavourites(article)
                }
                show()
            }
        }

    }

    ItemTouchHelper(itemTouchHelperCallback).apply{
        attachToRecyclerView(binding.recyclerFavourites)
    }
    newsViewModel.getFavouriteNews().observe(viewLifecycleOwner, Observer{articles ->

        newsAdapter.differ.submitList(articles)
    })
    }
    private fun setupFavouritesRecycler(){
        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply { this: RecyclerView
            adapter = newsAdapter
            LayoutManager = LinearLayoutManager(activity)
    }
    }