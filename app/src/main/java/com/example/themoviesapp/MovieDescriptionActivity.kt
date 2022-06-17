package com.example.themoviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.themoviesapp.databinding.ActivityMovieDescriptionBinding
import com.squareup.picasso.Picasso

class MovieDescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDescriptionBinding
    private val urlBackDropMovie: String = "https://image.tmdb.org/t/p/w500"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieOverView = getIntent().getStringExtra("overView")
        val movieTitle = getIntent().getStringExtra("title")
        val movieOriginalLanguage = getIntent().getStringExtra("originalLanguage")
        val moviePopularity = getIntent().getStringExtra("popularity")
        val movieReleaseDate = getIntent().getStringExtra("releaseDate")
        val movieBackdrop = getIntent().getStringExtra("backdrop")
        val poster = getIntent().getStringExtra("poster")

        binding.tvTitleMovieDescription.setText(movieTitle)
        binding.tvOverviewMovieDescription.setText(movieOverView)
        Picasso.get().load(urlBackDropMovie+movieBackdrop).into(binding.ivMovieDescription)
        binding.tvReleaseDateDescription.setText(movieReleaseDate)
        binding.tvLanguageDescription.setText(movieOriginalLanguage!!.uppercase())
        binding.tvPopularityDescription.setText(moviePopularity)
        Picasso.get().load(urlBackDropMovie+poster).into(binding.imPosterDescription)
    }
}