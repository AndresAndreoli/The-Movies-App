package com.example.themoviesapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.R
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.view.adapter.FavoriteMovieAdapter
import com.example.themoviesapp.view.adapter.MovieAdapter
import com.example.themoviesapp.viewmodel.ViewModelFavoriteMovies
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    // ViewModel
    private val viewModel : ViewModelFavoriteMovies by viewModels()

    // RecyclerView
    private var moviesList: MutableList<MovieItem> = mutableListOf()
    private lateinit var linearLayout: LinearLayoutManager
    private lateinit var adapter: FavoriteMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        setUpObservers()
    }

    private fun initComponents() {
        viewModel.getFavoriteMoviesFromDB()

        // Initializing variables
        linearLayout = LinearLayoutManager(requireContext())
        adapter = FavoriteMovieAdapter(moviesList)
    }

    private fun setUpObservers() {
        viewModel.favoriteMovies.observe(viewLifecycleOwner){
                moviesList.addAll(it)
                adapter.notifyDataSetChanged()
        }
    }
}