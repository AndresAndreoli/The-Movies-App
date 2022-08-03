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
import com.example.themoviesapp.databinding.ActivityMainBinding
import com.example.themoviesapp.services.APIService
import com.example.themoviesapp.viewmodel.ViewModelMovies
import com.example.themoviesapp.viewmodel.status
import com.example.themoviesapp.viewmodel.typeRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
//class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener{
class MainActivity : AppCompatActivity(){

    // Attributes
    private lateinit var binding: ActivityMainBinding
    private var movieDetailsList = mutableListOf<MovieDetailsResponse>()
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

        initRecyclerView()
        setUpListeners()

        //binding.svMovie.setOnQueryTextListener(this)


        /*binding.ivLoadContent.setOnClickListener {
            resetContent()
        }*/


        //loadRVWithMovies()
        //getGuestSessionId()

        binding.svMovie.clearFocus()
    }

    private fun initRecyclerView(){
        viewModel.onCreateMovies(typeRequest.CREATE, pageNum)
        viewModel.moviesStatus.observe(this, Observer {
            when (it){
                status.LOADING -> {
                    //TODO: loading
                    binding.ivLoadContent.visibility = View.GONE
                }
                status.SUCCESS -> {
                    viewModel.moviesList.observe(this, Observer {
                        binding.rvMovies.adapter = MovieAdapter(it, this)
                        binding.rvMovies.layoutManager = LinearLayoutManager(this)

                        binding.ivLoadContent.visibility = View.VISIBLE
                    })
                }
                status.ERROR -> {
                    //TODO: error
                    binding.ivLoadContent.visibility = View.GONE
                }
            }
        })
        /*binding.rvMovies.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount

                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        pageNum++
                        loadRVWithMovies()
                    }
                }
            }
        })*/
    }

    private fun setUpListeners(){
        binding.ivLoadContent.setOnClickListener {
            resetContent()
        }
    }

    private fun resetContent(){
        pageNum = 1
        viewModel.onCreateMovies(typeRequest.RESET, pageNum)
        binding.svMovie.clearFocus()
    }


    /*override fun onQueryTextSubmit(query: String?): Boolean {
        isLoading = true
        if (query!!.isNotEmpty()){
            var list = moviesList.filter {
                it.title.lowercase().contains(query.lowercase())
            }

            if (list.isEmpty()){
                var toast = Toast.makeText(this, "Sorry, We could not find your movie", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show()
                resetContent()
            } else {
                moviesList.clear()
                moviesList.addAll(list)
                binding.svMovie.clearFocus()
                adapter.notifyDataSetChanged()
            }
        }
        return false
    }*/

   /* override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }*/

    fun setFocus(view: View){
        binding.svMovie.onActionViewExpanded()
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