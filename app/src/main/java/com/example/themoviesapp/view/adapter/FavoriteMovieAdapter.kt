package com.example.themoviesapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.R
import com.example.themoviesapp.data.services.APIService
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.example.themoviesapp.domain.model.MovieItem

class FavoriteMovieAdapter(private var favoriteMovies: List<MovieItem>) :
    RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    class FavoriteMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemMovieBinding.bind(itemView)

        fun render(movie: MovieItem) {
            Glide.with(binding.cvMovieContainer.context)
                .load(APIService.urlImage+movie.poster_path)
                .into(binding.ivImageMain)
            binding.tvTitleMain.setText(movie.title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        return FavoriteMovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val item = favoriteMovies[position]
        holder.render(item)
    }

    override fun getItemCount() = favoriteMovies.size

}