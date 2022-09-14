package com.example.themoviesapp.view.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.R
import com.example.themoviesapp.domain.model.MovieItem

class MovieAdapter(
    private var movieModels: List<MovieItem>,
    private val onClickListener: (idMovie: Int) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_popular_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movieModels[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount() = if (movieModels.size>10) 10 else movieModels.size
}