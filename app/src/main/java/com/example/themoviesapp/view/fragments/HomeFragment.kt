package com.example.themoviesapp.view

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.FragmentHomeBinding
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.utils.KindOfFragment
import com.example.themoviesapp.utils.ValuesProvider
import com.example.themoviesapp.view.adapter.favorite.FavoriteMovieAdapter
import com.example.themoviesapp.view.adapter.home.MovieAdapter
import com.example.themoviesapp.viewmodel.ViewModelMovies
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    // Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: ViewModelMovies by viewModels()

    // RecyclerView
    private var popularMoviesList: MutableList<MovieItem> = mutableListOf()
    private var topRatedMoviesList: MutableList<MovieItem> = mutableListOf()
    private var upcomingMoviesList: MutableList<MovieItem> = mutableListOf()
    private lateinit var adapterPopular: MovieAdapter
    private lateinit var adapterTopRated: MovieAdapter
    private lateinit var adapterUpcoming: MovieAdapter
    private var isLoading = false
    private var pageNum = 1

    private lateinit var navBar: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            // destruyo el binding cuando destruyo la vista del fragment, ya que puedo tener
            // error cuando uso un liveData, por ejemplo, si intento poblar un recyclerView
        }
        initComponents()
    }

    // Start all components
    private fun initComponents() {
        // Show botton navigation bar
        navBar = requireActivity().findViewById(R.id.bnvMainActivity)
        navBar.visibility = View.VISIBLE

        // Initializing variables
        adapterPopular = MovieAdapter(popularMoviesList) {
            onMovieSelected(it)
        }

        adapterTopRated = MovieAdapter(topRatedMoviesList) {
            onMovieSelected(it)
        }

        adapterUpcoming = MovieAdapter(upcomingMoviesList) {
            onMovieSelected(it)
        }

        //binding.svMovie.setOnQueryTextListener(this)
        //binding.svMovie.clearFocus()

        setUpObservers()
        setUpListeners()
        initRecyclerViewPopulateMovies()
    }

    private fun initRecyclerViewPopulateMovies() {
        viewModel.onCreateMovies(pageNum)
        binding.rvPopularMovies.adapter = adapterPopular

        binding.rvTopRatedMovies.adapter = adapterTopRated

        binding.rvUpcomingMovies.adapter = adapterUpcoming
    }

    private fun setUpListeners() {
        /*binding.ivLoadContent.setOnClickListener {
            resetContent()
        }*/

        /*binding.svMovie.setOnClickListener {
            binding.svMovie.onActionViewExpanded()
        }*/

        /*binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = linearLayout.childCount
                val pastVisibleItem = linearLayout.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount

                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        binding.pbLoadItems.visibility = View.VISIBLE // show SPINNER
                        pageNum++
                        viewModel.loadMoreMovies(pageNum)
                        //binding.ivLoadContent.visibility = View.GONE
                    }
                }
            }
        })*/

        binding.btnRetryMoviesCall.setOnClickListener {
            //resetContent()
        }
    }

    // reseting content main screen and cleaning cache
    /*private fun resetContent() {
        pageNum = 1
        var clear = viewModel.clearCache()
        moviesList.clear()
        if (clear) {
            showSnackBar(
                resources.getString(R.string.successClearingMovies),
                resources.getColor(R.color.green)
            )
            viewModel.onCreateMovies(pageNum)
            //binding.svMovie.clearFocus()
        } else {
            showSnackBar(
                resources.getString(R.string.errorClearingMovies),
                resources.getColor(R.color.red)
            )
        }
    }*/

    // displays a generic message
    private fun showSnackBar(message: String, color: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(color)
            .show()
    }

    private fun setUpObservers() {
        viewModel.popularMoviesList.observe(viewLifecycleOwner, Observer {
            popularMoviesList.clear()
            popularMoviesList.addAll(it)
            adapterPopular.notifyDataSetChanged()
        })

        viewModel.topRatedMoviesList.observe(viewLifecycleOwner, Observer {
            topRatedMoviesList.clear()
            topRatedMoviesList.addAll(it)
            adapterTopRated.notifyDataSetChanged()
        })

        viewModel.upcomingMoviesList.observe(viewLifecycleOwner, Observer {
            upcomingMoviesList.clear()
            upcomingMoviesList.addAll(it)
            adapterUpcoming.notifyDataSetChanged()
        })

        viewModel.moviesStatus.observe(viewLifecycleOwner, Observer {
            when (it.load) {
                ValuesProvider.Status.LOADING -> {
                    when (it.type){
                        ValuesProvider.typeRequest.POPULAR -> {
                            //binding.rvPopularMovies.visibility = View.GONE
                        }
                        ValuesProvider.typeRequest.TOP_RATED -> {
                            //binding.rvTopRatedMovies.visibility = View.GONE
                        }
                        ValuesProvider.typeRequest.UPCOMING -> {
                            binding.rvUpcomingMovies.visibility = View.GONE
                        }
                    }
                    //binding.ivLoadContent.visibility = View.GONE
                    //binding.pbLoadItems.visibility = View.VISIBLE
                    //binding.llErrorMovieCall.visibility = View.GONE
                }
                ValuesProvider.Status.SUCCESS -> {
                    when (it.type){
                        ValuesProvider.typeRequest.POPULAR -> {
                            binding.rvPopularMovies.visibility = View.VISIBLE
                        }
                        ValuesProvider.typeRequest.TOP_RATED -> {
                            binding.rvTopRatedMovies.visibility = View.VISIBLE
                        }
                        ValuesProvider.typeRequest.UPCOMING -> {
                            binding.rvUpcomingMovies.visibility = View.VISIBLE
                        }
                    }
                    //binding.ivLoadContent.visibility = View.VISIBLE // reset button
                    //binding.pbLoadItems.visibility = View.GONE // progress bar
                }
                ValuesProvider.Status.ERROR -> {
                    when (it.type){
                        ValuesProvider.typeRequest.POPULAR -> {
                            binding.rvPopularMovies.visibility = View.GONE
                        }
                        ValuesProvider.typeRequest.TOP_RATED -> {
                            binding.rvTopRatedMovies.visibility = View.GONE
                        }
                        ValuesProvider.typeRequest.UPCOMING -> {
                            binding.rvUpcomingMovies.visibility = View.GONE
                        }
                    }
                    //binding.ivLoadContent.visibility = View.GONE
                    //binding.pbLoadItems.visibility = View.GONE
                    //binding.llErrorMovieCall.visibility = View.VISIBLE
                }
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            isLoading = it
        })
    }

    // searchView: search by movie title
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.isNotEmpty()) {
            //moviesList.clear()
            //var movieFounded = viewModel.searchMovie(query)

            /*if (!movieFounded) {
                var toast = Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.movieNotFound),
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show()
                //viewModel.retrieveMoviesFromCache()
            }*/
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    // Function to open the description movie fragment
    private fun onMovieSelected(idMovie: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDescriptionMovieFragment(idMovie, KindOfFragment.HOME_FRAGMENT)
        findNavController().navigate(action)

        // Hide navigation bar
        navBar.visibility = View.GONE
    }

}