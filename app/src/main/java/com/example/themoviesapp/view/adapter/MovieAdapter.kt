package com.example.themoviesapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.services.APIService

class MovieAdapter(
    private var movies: List<Movie>,
    private val onClickListener: (idMovie: Int) -> Unit
    ): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemMovieBinding.bind(itemView)

        fun render (
            movie: Movie,
            onClickListener: (idMovie: Int) -> Unit
        ){
            Glide.with(binding.cvMovieContainer.context)
                .load(APIService.urlImage+movie.poster_path)
                .into(binding.ivImageMain)
            binding.tvTitleMain.setText(movie.title)

            binding.cvMovieContainer.setOnClickListener {
                onClickListener(movie.id ?: -1)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}