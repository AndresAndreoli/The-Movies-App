package com.example.themoviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // Attributes
    private lateinit var binding: ActivityMainBinding
    private val baseUrl: String = "https://api.themoviedb.org/3/movie/"
    private lateinit var adapter: MovieAdapter
    private var moviesList = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        loadRVWithMovies()
    }

    private fun initRecyclerView(){
        adapter = MovieAdapter(moviesList, this)
        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        binding.rvMovies.adapter = adapter
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadRVWithMovies(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getMovies("popular?api_key=208e554046f1cf82cd9a3dd3e315fe5f&language=en-US&page=1")
            val movie = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    val movies = movie?.movies ?: emptyList()
                    moviesList.clear()
                    moviesList.addAll(movies)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}