package com.example.themoviesapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.FragmentFavoriteBinding
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.utils.KindOfFragment
import com.example.themoviesapp.view.HomeFragmentDirections
import com.example.themoviesapp.view.adapter.FavoriteMovieAdapter
import com.example.themoviesapp.viewmodel.ViewModelFavoriteMovies
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    //dataBinding
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel : ViewModelFavoriteMovies by viewModels()

    // RecyclerView
    private var moviesList: MutableList<MovieItem> = mutableListOf()
    private lateinit var linearLayout: LinearLayoutManager
    private lateinit var adapter: FavoriteMovieAdapter

    private lateinit var navBar: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        initComponents()
        setUpObservers()
        initRecyclerViewFavoriteMovies()
    }

    private fun initRecyclerViewFavoriteMovies() {
        binding.rvFavoriteMovies.adapter = adapter
        binding.rvFavoriteMovies.layoutManager = linearLayout
    }

    private fun initComponents() {
        viewModel.getFavoriteMoviesFromDB()

        // Show botton navigation bar
        navBar = requireActivity().findViewById(R.id.bnvMainActivity)
        navBar.visibility = View.VISIBLE

        // Initializing variables
        linearLayout = LinearLayoutManager(requireContext())
        adapter = FavoriteMovieAdapter(moviesList){
            onMovieSelected(it)
        }
    }

    private fun setUpObservers() {
        viewModel.favoriteMovies.observe(viewLifecycleOwner){
                moviesList.addAll(it)
                adapter.notifyDataSetChanged()
        }
    }

    // Function to open the description movie fragment
    private fun onMovieSelected(idMovie: Int) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDescriptionMovieFragment(idMovie, KindOfFragment.FAVORITE_FRAGMENT)
        findNavController().navigate(action)

        // Hide navigation bar
        navBar.visibility = View.GONE
    }
}