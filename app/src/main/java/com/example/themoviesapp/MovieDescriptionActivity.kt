package com.example.themoviesapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviesapp.MainActivity.Companion.movieDetailsList
import com.example.themoviesapp.databinding.ActivityMovieDescriptionBinding
import com.example.themoviesapp.services.APIService
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class MovieDescriptionActivity : AppCompatActivity() {

    //Atributes
    private lateinit var binding: ActivityMovieDescriptionBinding
    //private var edit = MainActivity.sharedPreferences.edit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idMovie: String = getIntent().getStringExtra("ID").toString()
        loadDetailsMovie(idMovie)

        binding.rbRate.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if (!binding.btnRate.isEnabled){
                binding.btnRate.isEnabled = true
            }
        }

        binding.btnRate.setOnClickListener{
            rateMovie(idMovie)
        }
    }

    private fun getRetrofit(url: String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(APIService.urlEndPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
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

        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit(APIService.urlEndPoint).create(APIService::class.java).getDetailsMovie(query)
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

            binding.tvDurationDescription.setText(converterTime(it.runtime))
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

    private fun rateMovie(idMovie: String){
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
        }

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
}