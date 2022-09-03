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
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.FragmentHomeBinding
import com.example.themoviesapp.model.movieResponse.MovieModel
import com.example.themoviesapp.view.adapter.MovieAdapter
import com.example.themoviesapp.viewmodel.ValuesProvider
import com.example.themoviesapp.viewmodel.ViewModelMovies
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class homeFragment : Fragment(), SearchView.OnQueryTextListener{

    // Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: ViewModelMovies by viewModels()

    // RecyclerView
    private var moviesList: MutableList<MovieModel> = mutableListOf()
    private lateinit var linearLayout: LinearLayoutManager
    private lateinit var adapter: MovieAdapter
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
    private fun initComponents(){
        navBar = requireActivity().findViewById(R.id.bnvMainActivity)

        // Show botton navigation bar
        navBar.visibility = View.VISIBLE

        // Initializing variables
        linearLayout = LinearLayoutManager(requireContext())
        adapter = MovieAdapter(moviesList){
            onMovieSelected(it)
        }

        binding.svMovie.setOnQueryTextListener(this)
        binding.svMovie.clearFocus()

        initRecyclerViewPopulateMovies()
        setUpListeners()
        setUpObservers()
    }

    private fun initRecyclerViewPopulateMovies() {
        viewModel.onCreateMovies(pageNum)
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = linearLayout
    }

    private fun setUpListeners(){
        binding.ivLoadContent.setOnClickListener {
            resetContent()
        }

        binding.svMovie.setOnClickListener {
            binding.svMovie.onActionViewExpanded()
        }

        binding.rvMovies.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
                        binding.ivLoadContent.visibility = View.GONE
                    }
                }
            }
        })

        binding.btnRetryMoviesCall.setOnClickListener {
            resetContent()
        }
    }

    // reseting content main screen and cleaning cache
    private fun resetContent(){
        pageNum = 1
        var clear = viewModel.clearCache()
        moviesList.clear()
        if (clear){
            showSnackBar(resources.getString(R.string.successClearingMovies), resources.getColor(R.color.green))
            viewModel.onCreateMovies(pageNum)
            binding.svMovie.clearFocus()
        } else {
            showSnackBar(resources.getString(R.string.errorClearingMovies), resources.getColor(R.color.red))
        }
    }

    // displays a generic message
    private fun showSnackBar(message: String, color: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(color)
            .show()
    }

    private fun setUpObservers() {
        viewModel.moviesList.observe(viewLifecycleOwner, Observer {
            moviesList.clear()
            moviesList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.moviesStatus.observe(viewLifecycleOwner, Observer {
            when (it){
                ValuesProvider.Status.LOADING -> {
                    binding.ivLoadContent.visibility = View.GONE
                    binding.rvMovies.visibility = View.GONE
                    binding.pbLoadItems.visibility = View.VISIBLE
                    binding.llErrorMovieCall.visibility = View.GONE
                }
                ValuesProvider.Status.SUCCESS -> {
                    binding.ivLoadContent.visibility = View.VISIBLE // reset button
                    binding.rvMovies.visibility = View.VISIBLE // recyclerView
                    binding.pbLoadItems.visibility = View.GONE // progress bar
                }
                ValuesProvider.Status.ERROR -> {
                    binding.ivLoadContent.visibility = View.GONE
                    binding.rvMovies.visibility = View.GONE
                    binding.pbLoadItems.visibility = View.GONE
                    binding.llErrorMovieCall.visibility = View.VISIBLE
                }
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            isLoading = it
        })
    }

    // searchView: search by movie title
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.isNotEmpty()){
            moviesList.clear()
            var movieFounded = viewModel.searchMovie(query)

            if (!movieFounded){
                var toast = Toast.makeText(requireContext(), resources.getString(R.string.movieNotFound), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show()
                viewModel.retrieveMoviesFromCache()
            }
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    // Function to open the description movie fragment
    private fun onMovieSelected(idMovie: Int){
        val action = homeFragmentDirections.actionHomeFragmentToDescriptionMovieFragment(idMovie)
        findNavController().navigate(action)

        // Hide navigation bar
        navBar.visibility = View.GONE
    }
}