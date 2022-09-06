package com.example.themoviesapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.FragmentDescriptionMovieBinding
import com.example.themoviesapp.data.services.APIService
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.utils.KindOfFragment
import com.example.themoviesapp.utils.ValuesProvider
import com.example.themoviesapp.viewmodel.ViewModelMovieDetails
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class DescriptionMovieFragment : Fragment() {
    private var _binding : FragmentDescriptionMovieBinding? = null
    private val binding get() = _binding!!

    private val args: DescriptionMovieFragmentArgs by navArgs()
    private val viewModel: ViewModelMovieDetails by viewModels()

    private lateinit var movieDetails: MovieDetailsResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDescriptionMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        initComponents()
    }

    private fun initComponents() {
        // request movie details
        viewModel.onCreateMovieDetails(args.idMovie)

        binding.rbRate.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if (!binding.btnRate.isEnabled){
                binding.btnRate.isEnabled = true
            }
        }

        binding.btnRate.setOnClickListener{
            //rateMovie(idMovie)
        }

        setUpObservers()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.tbFavoriteMovie.setOnClickListener {
            viewModel.insertFavoriteMovieToDB(MovieItem(
                movieDetails.adult,
                movieDetails.backdrop_path,
                listOf(1,2),
                movieDetails.id,
                movieDetails.original_language,
                movieDetails.original_title,
                movieDetails.overview,
                movieDetails.popularity,
                movieDetails.poster_path,
                movieDetails.release_date,
                movieDetails.title,
                movieDetails.video,
                movieDetails.vote_average,
                movieDetails.vote_count
            ))
        }
    }

    private fun setUpObservers() {
        viewModel.movieDetailStatus.observe(viewLifecycleOwner){
            when (it){
                ValuesProvider.Status.LOADING -> {}
                ValuesProvider.Status.SUCCESS -> {
                    viewModel.movieDetails.observe(viewLifecycleOwner){
                        movieDetails = it
                        loadMovieContentToFragment(it)
                    }
                }
                ValuesProvider.Status.ERROR -> {
                    when (args.fragmentBefore){
                        KindOfFragment.HOME_FRAGMENT -> {
                            findNavController().navigate(DescriptionMovieFragmentDirections.actionDescriptionMovieFragmentToHomeFragment())
                        }
                        KindOfFragment.FAVORITE_FRAGMENT -> {
                            findNavController().navigate(DescriptionMovieFragmentDirections.actionDescriptionMovieFragmentToFavoriteFragment())
                        }
                    }
                    Toast.makeText(requireContext(), "No se pudo encontrar la pelicula", Toast.LENGTH_LONG).show()
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

    private fun loadMovieContentToFragment(movie: MovieDetailsResponse){
        movie.let {
            // Imprime los datos en la Movie Description Activity
            binding.tvTitleMovieDescription.setText(it.title)
            binding.tvOverviewMovieDescription.setText(it.overview)
            Glide.with(this)
                .load(APIService.urlImage+it.backdrop_path)
                .placeholder(R.drawable.progress_animation)
                .into(binding.ivMovieBackDropDescription)
            binding.tvReleaseDateDescription.setText(it.release_date)
            binding.tvLanguageDescription.setText(it.original_language!!.uppercase())
            binding.tvPopularityDescription.setText((it.popularity).toString())
            binding.tvVoteAverageDescription.setText("${(DecimalFormat("#.#").format(it.vote_average))}/10")
            Glide.with(this)
                .load(APIService.urlImage+it.poster_path)
                .placeholder(R.drawable.progress_animation)
                .into(binding.imPosterDescription)

            var genresContainer = ""
            it.genres.forEach { genre ->
                genresContainer += "${genre.name}, "
            }
            binding.tvGenresDescription.setText(genresContainer.dropLast(2))

            if (it.adult)
                binding.tvAdultDescription.setText("18+")
            else
                binding.tvAdultDescription.setText("All ages")

            binding.tvDurationDescription.setText(converterTime(it.runtime!!))
        }
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