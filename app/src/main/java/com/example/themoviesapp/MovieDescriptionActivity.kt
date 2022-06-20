package com.example.themoviesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviesapp.databinding.ActivityMovieDescriptionBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class MovieDescriptionActivity : AppCompatActivity() {

    //Atributes
    private lateinit var binding: ActivityMovieDescriptionBinding
    private val urlBackDropMovie: String = "https://image.tmdb.org/t/p/w500"
    private val baseUrl: String = "https://api.themoviedb.org/3/movie/"
    private lateinit var idMovie: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMovie = getIntent().getStringExtra("ID").toString()
        loadDetailsMovie()
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadDetailsMovie(){
        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit().create(APIService::class.java).getDetailsMovie("${baseUrl+idMovie}?api_key=208e554046f1cf82cd9a3dd3e315fe5f&language=en-US")
            val movieDetail = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    movieDetail.let {
                        binding.tvTitleMovieDescription.setText(it?.title ?: "No result")
                        binding.tvOverviewMovieDescription.setText(it?.overview ?: "No result")
                        Picasso.get().load(urlBackDropMovie+it!!.backdrop_path).into(binding.ivMovieBackDropDescription)
                        binding.tvReleaseDateDescription.setText(it?.release_date  ?: "No result")
                        binding.tvLanguageDescription.setText(it?.original_language!!.uppercase()  ?: "No result")
                        binding.tvPopularityDescription.setText((it?.popularity ?: "No result").toString())
                        Picasso.get().load(urlBackDropMovie+it.poster_path).into(binding.imPosterDescription)

                        var genresContainer: String = ""
                        it.genres.forEach { genre ->
                            genresContainer += "${genre.name}, "
                        }
                        binding.tvGenresDescription.setText(genresContainer.dropLast(2))

                        if (it.adult)
                            binding.tvAdultDescription.setText("+18")
                        else
                            binding.tvAdultDescription.setText("All ages")

                        binding.tvDurationDescription.setText(converterTime(it.runtime))
                    }
                } else {
                    Toast.makeText(this@MovieDescriptionActivity, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
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
}