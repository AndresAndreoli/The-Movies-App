package com.example.themoviesapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moviesList.clear()
        binding.svMovie.setOnQueryTextListener(this)
        layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(moviesList, this)

        binding.ivLoadContent.setOnClickListener {
            resetContent()
        }

        initRecyclerView()
        loadRVWithMovies()
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

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(APIService.urlEndPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadRVWithMovies(){
        binding.pbLoadItems.visibility = View.VISIBLE
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getMovies("popular?api_key=${APIService.APIkey}&language=en-US&page=${pageNum}")
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
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        isLoading = true
        if (query!!.isNotEmpty()){
            var list = moviesList.filter {
                it.title.lowercase().contains(query.lowercase())
            }

            moviesList.clear()
            moviesList.addAll(list)
            adapter.notifyDataSetChanged()
            binding.svMovie.clearFocus()

            if (list.isEmpty()){
                var toast = Toast.makeText(this, "Sorry, I could not found your movie", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show()
                resetContent()
            }
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    fun setFocus(view: View){
        binding.svMovie.onActionViewExpanded()
    }

    private fun resetContent(){
        moviesList.clear()
        pageNum = 1
        loadRVWithMovies()
        binding.svMovie.clearFocus()
    }
}