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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.view.adapter.MovieAdapter
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.ActivityMainBinding
import com.example.themoviesapp.model.Cache
import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.services.APIService
import com.example.themoviesapp.viewmodel.ViewModelMovies
import com.example.themoviesapp.viewmodel.Status
import com.example.themoviesapp.viewmodel.TypeRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener{

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

        initRecyclerView()
        setUpListeners()

        binding.svMovie.setOnQueryTextListener(this)

        //loadRVWithMovies()
        //getGuestSessionId()

        binding.svMovie.clearFocus()

        viewModel.isLoading.observe(this, Observer {
            isLoading = it
        })
    }

    private fun initRecyclerView(){
        viewModel.onCreateMovies(TypeRequest.CREATE, pageNum)
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = linearLayout

        viewModel.moviesStatus.observe(this, Observer {
            when (it){
                Status.LOADING -> {
                    binding.ivLoadContent.visibility = View.GONE
                    binding.rvMovies.visibility = View.GONE
                    binding.pbLoadItems.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    viewModel.moviesList.observe(this, Observer {
                        // It: only return 20 movies
                        moviesList.addAll(it)
                        adapter.notifyDataSetChanged()
                        binding.ivLoadContent.visibility = View.VISIBLE // reset button
                        binding.rvMovies.visibility = View.VISIBLE // recyclerView
                        binding.pbLoadItems.visibility = View.GONE // progres bar
                    })
                }
                Status.ERROR -> {
                    // TODO: retry button
                    binding.ivLoadContent.visibility = View.GONE
                    binding.rvMovies.visibility = View.GONE
                }
            }
        })
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
                    }
                }
            }
        })
    }

    // reseting content main screen and cleaning cache
    private fun resetContent(){
        pageNum = 1
        var clear = viewModel.clearCache()
        if (clear){
            showSnackBar(resources.getString(R.string.successClearingMovies), resources.getColor(R.color.green))
            viewModel.onCreateMovies(TypeRequest.RESET, pageNum)
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
                var toast = Toast.makeText(this, "Sorry, We could not find your movie", Toast.LENGTH_LONG)
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

    /*private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }*/

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
}