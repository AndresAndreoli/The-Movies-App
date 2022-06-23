package com.example.themoviesapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviesapp.MainActivity.Companion.movieDetailsList
import com.example.themoviesapp.databinding.ActivityMovieDescriptionBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDescriptionActivity : AppCompatActivity() {

    //Atributes
    private lateinit var binding: ActivityMovieDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idMovie: String = getIntent().getStringExtra("ID").toString()
        loadDetailsMovie(idMovie)
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(APIService.urlEndPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadDetailsMovie(query: String){
        movieDetailsList.forEach {
            if (it.id == query.toInt()){
                showDetails(it)
                return
            }
        }

        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit().create(APIService::class.java).getDetailsMovie(query)
            val movieDetail = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    if (movieDetail == null){
                        errorMessage()
                    } else {
                        showDetails(movieDetail)
                        movieDetailsList.add(movieDetail)
                    }
                } else {
                    errorMessage()
                }
            }
        }
    }

    private fun converterTime(minutes: Int): String{
        var horas: Int = 0
        var min: Int = minutes

        while (min>60){
            horas++
            min -= 60
        }

        return "${horas}:${min}"
    }

    private fun showDetails(movie: MovieDetailsResponse){
        movie.let {
            // Imprime los datos en la Movie Description Activity
            binding.tvTitleMovieDescription.setText(it.title)
            binding.tvOverviewMovieDescription.setText(it.overview)
            Picasso.get().load(APIService.urlImage+it.backdrop_path).into(binding.ivMovieBackDropDescription)
            binding.tvReleaseDateDescription.setText(it.release_date)
            binding.tvLanguageDescription.setText(it.original_language!!.uppercase())
            binding.tvPopularityDescription.setText((it.popularity).toString())
            binding.tvVoteAverageDescription.setText("${(it.vote_average).toString()}/10")
            Picasso.get().load(APIService.urlImage+it.poster_path).into(binding.imPosterDescription)

            var genresContainer: String = ""
            it.genres.forEach { genre ->
                genresContainer += "${genre.name}, "
            }
            binding.tvGenresDescription.setText(genresContainer.dropLast(2))

            if (it.adult)
                binding.tvAdultDescription.setText("18+")
            else
                binding.tvAdultDescription.setText("All ages")

            binding.tvDurationDescription.setText(converterTime(it.runtime))
        }
    }

    private fun errorMessage(){
        Toast.makeText(this, "Error. The movie could not be loaded", Toast.LENGTH_LONG).show()
        finish()
    }
}