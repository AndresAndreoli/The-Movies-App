package com.example.themoviesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(private var movies: List<Movie>): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val urlImage = "https://image.tmdb.org/t/p/w500"

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemMovieBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]

        Picasso.get().load(urlImage+item.poster_path).into(holder.binding.ivMovie)
    }

    override fun getItemCount(): Int =  movies.size
}