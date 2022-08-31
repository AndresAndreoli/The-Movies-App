package com.example.themoviesapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themoviesapp.view.MovieDescriptionActivity
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.services.APIService

class MovieAdapter(private var movies: List<Movie>): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemMovieBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]

        Glide.with(holder.binding.cvMovieContainer.context)
            .load(APIService.urlImage+item.poster_path)
            .into(holder.binding.ivImageMain)
        holder.binding.tvTitleMain.setText(item.title)

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}