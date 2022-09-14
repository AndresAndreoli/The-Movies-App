package com.example.themoviesapp.view.adapter.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.R
import com.example.themoviesapp.domain.model.MovieItem

class FavoriteMovieAdapter(
    private var movieModels: List<MovieItem>,
    private val onClickListener: (idMovie: Int) -> Unit
) : RecyclerView.Adapter<FavoriteMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        return FavoriteMovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holderFavorite: FavoriteMovieViewHolder, position: Int) {
        val item = movieModels[position]
        // Animation slides from left
        val animation : Animation = AnimationUtils.loadAnimation(holderFavorite.itemView.context, android.R.anim.slide_in_left)
        holderFavorite.itemView.startAnimation(animation)
        holderFavorite.render(item, onClickListener)
    }

    override fun getItemCount() = movieModels.size
}