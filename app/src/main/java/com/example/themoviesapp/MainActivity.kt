package com.example.themoviesapp

import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener{

    // Attributes
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    private var moviesList = mutableListOf<Movie>()
    private lateinit var layoutManager: LinearLayoutManager
    private var movieDetailsList = mutableListOf<MovieDetailsResponse>()

    private var isLoading = false
    private var pageNum = 1

    companion object{
        // Contiene los detalles de las peliculas ya vistas
        var movieDetailsList = mutableListOf<MovieDetailsResponse>()

        //lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moviesList.clear()
        binding.svMovie.setOnQueryTextListener(this)
        layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(moviesList, this)
        //sharedPreferences =  getSharedPreferences("RATE_INFO", Context.MODE_PRIVATE)

        binding.ivLoadContent.setOnClickListener {
            resetContent()
        }

        initRecyclerView()
        loadRVWithMovies()
        getGuestSessionId()

        binding.svMovie.clearFocus()
    }

    private fun initRecyclerView(){
        binding.rvMovies.layoutManager = layoutManager
        binding.rvMovies.adapter = adapter

        binding.rvMovies.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
        })
    }

    private fun getRetrofit(url: String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadRVWithMovies(){
        if (!isOnline()){
            // Tendria que implementar un broadcastReceivers, para que, cuando haya o no internet, la app cargue o no los datos segun
            // el estado de Internet. (no llegue con los tiempos para hacerlo)
            Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show()
            return
        }
        binding.pbLoadItems.visibility = View.VISIBLE
        binding.ivLoadContent.visibility = View.INVISIBLE
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit(APIService.urlEndPoint).create(APIService::class.java).getMovies("popular?api_key=${APIService.APIkey}&language=en-US&page=${pageNum}")
            val movie = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    val movies = movie?.movies ?: emptyList()
                    moviesList.addAll(movies)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "We could not find more movies", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
                binding.pbLoadItems.visibility = View.GONE
                binding.ivLoadContent.visibility = View.VISIBLE
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
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
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    fun setFocus(view: View){
        binding.svMovie.onActionViewExpanded()
    }

    private fun resetContent(){
        moviesList.clear()
        pageNum = 1
        loadRVWithMovies()
        binding.svMovie.clearFocus()
        adapter.notifyDataSetChanged()
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun getGuestSessionId(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit(APIService.urlAuthentication).create(APIService::class.java).getGuestSessionID(APIService.APIkey)
            val guestSession = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    APIService.guest_session_id = guestSession!!.guest_session_id
                } else {
                    Toast.makeText(this@MainActivity, "We could not get the guest session ID ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}