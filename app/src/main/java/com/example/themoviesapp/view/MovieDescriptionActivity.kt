package com.example.themoviesapp.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.RateObject
import com.example.themoviesapp.view.MainActivity.Companion.movieDetailsList
import com.example.themoviesapp.databinding.ActivityMovieDescriptionBinding
import com.example.themoviesapp.services.APIService
import com.example.themoviesapp.viewmodel.ViewModelMovieDetails
import com.example.themoviesapp.viewmodel.ViewModelMovies
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

@AndroidEntryPoint
class MovieDescriptionActivity : AppCompatActivity() {

    //Atributes
    private lateinit var binding: ActivityMovieDescriptionBinding
    private val viewModel: ViewModelMovieDetails by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idMovie: Int = getIntent().getStringExtra("ID")!!.toInt()
        viewModel.onCreateMovieDetails(idMovie)

        //loadDetailsMovie(idMovie)

        binding.rbRate.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if (!binding.btnRate.isEnabled){
                binding.btnRate.isEnabled = true
            }
        }

        binding.btnRate.setOnClickListener{
            //rateMovie(idMovie)
        }
    }


    private fun loadDetailsMovie(query: String){
        movieDetailsList.forEach {
            if (it.id == query.toInt()){
                showDetails(it)

                /*var retrieveRate = MainActivity.sharedPreferences.getFloat(query, -1F)

                if (retrieveRate!=-1F){
                    binding.btnRate.isEnabled = false
                    binding.rbRate.rating = retrieveRate
                    binding.rbRate.isEnabled = false
                } else {
                    println("It isn't")
                }*/
                return
            }
        }

        if (!isOnline()){
            Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show()
            return
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
            binding.tvVoteAverageDescription.setText("${(DecimalFormat("#.#").format(it.vote_average)).toString()}/10")
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

            //binding.tvDurationDescription.setText(converterTime(it.runtime))
        }
    }

    private fun errorMessage(){
        Toast.makeText(this, "Error. The movie could not be loaded", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    /*private fun rateMovie(idMovie: String){
        var rateObject = RateObject(binding.rbRate.rating)
        var rate = binding.rbRate.rating
        binding.btnRate.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit(APIService.urlEndPoint).create(APIService::class.java).postMovie(idMovie.toInt(), APIService.APIkey, APIService.guest_session_id, rateObject)
            val rateRes = call.body()
            runOnUiThread{
                if (call.code() == 201){
                    Toast.makeText(this@MovieDescriptionActivity, "Rated successfully", Toast.LENGTH_LONG).show()
                    //edit.putFloat(idMovie, rate)
                } else {
                    Toast.makeText(this@MovieDescriptionActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                }
                binding.btnRate.isEnabled = true
            }
        }*/

        /* Esta es una implementacion que decidi implementar al principio, pero luego decidi implementar la de arriba
           para poder usar las corrutinas

        val requestObject = getRetrofit().create(APIService::class.java).postMovie(rateObject)

        // enqueue: nos permite realizar la petición de manera asincrónica
        // execute: nos permite realizar la petición de manera sincrónica
        requestObject.enqueue(object: Callback<MovieRateResponse>{
            override fun onResponse(call: Call<MovieRateResponse>, response: Response<MovieRateResponse>) {
                if (response.code()==201){
                    Toast.makeText(this@MovieDescriptionActivity, "Bien", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MovieDescriptionActivity, "Mal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieRateResponse>, t: Throwable) {
                println("fallo")
            }

        })*/
}