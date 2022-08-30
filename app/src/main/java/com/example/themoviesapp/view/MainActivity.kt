package com.example.themoviesapp.view

import android.annotation.SuppressLint
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.view.adapter.MovieAdapter
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.ActivityMainBinding
import com.example.themoviesapp.model.Cache
import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.services.APIService
import com.example.themoviesapp.viewmodel.ValuesProvider
import com.example.themoviesapp.viewmodel.ViewModelMovies
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    // Attributes
    private lateinit var binding: ActivityMainBinding
    private lateinit var linearLayout: LinearLayoutManager
    private var moviesList: MutableList<Movie> = mutableListOf()
    private lateinit var adapter: MovieAdapter
    private val viewModel: ViewModelMovies by viewModels()

    private var isLoading = false
    private var pageNum = 1

    companion object{
        // Contiene los detalles de las peliculas ya vistas
        var movieDetailsList = mutableListOf<MovieDetailsResponse>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing variables
        linearLayout = LinearLayoutManager(this)
        adapter = MovieAdapter(moviesList, this)

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = binding.navHostFragments.getFragment() as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvMainActivity.setupWithNavController(navController)

        //initRecyclerView()
        //setUpListeners()
        //setUpObservers()

        //binding.svMovie.setOnQueryTextListener(this)

        //binding.svMovie.clearFocus()

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

   /* override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun initRecyclerView(){
        viewModel.onCreateMovies(pageNum)
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = linearLayout
    }

    private fun setUpListeners(){
        binding.ivLoadContent.setOnClickListener {
            resetContent()
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

    private fun setUpObservers() {
        viewModel.moviesList.observe(this, Observer {
            moviesList.clear()
            moviesList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.moviesStatus.observe(this, Observer {
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

        viewModel.isLoading.observe(this, Observer {
            isLoading = it
        })
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

    // searchView: search by movie title
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.isNotEmpty()){
            moviesList.clear()
            var movieFounded = viewModel.searchMovie(query)

            if (!movieFounded){
                var toast = Toast.makeText(this, resources.getString(R.string.movieNotFound), Toast.LENGTH_LONG)
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

    fun setFocus(view: View){
        binding.svMovie.onActionViewExpanded()
    }

    // displays a generic message
    private fun showSnackBar(message: String, color: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(color)
            .show()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean){
        if (isConnected){
            viewModel.connectionInternet(true)
            binding.llNoInternet.visibility = View.GONE
            binding.ivLoadContent.visibility = View.VISIBLE
        } else {
            viewModel.connectionInternet(false)
            showSnackBar("No connection", resources.getColor(R.color.warn_red))
            binding.llNoInternet.visibility = View.VISIBLE
            binding.ivLoadContent.visibility = View.GONE
        }
    }

    /*private fun getGuestSessionId(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit(APIService.urlAuthentication).create(APIService::class.java).getGuestSessionID(
                APIService.APIkey)
            val guestSession = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    APIService.guest_session_id = guestSession!!.guest_session_id
                } else {
                    Toast.makeText(this@MainActivity, "We could not get the guest session ID ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/

    */
}