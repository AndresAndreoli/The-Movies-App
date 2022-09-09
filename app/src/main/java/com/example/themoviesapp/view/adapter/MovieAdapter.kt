package com.example.themoviesapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.R
import com.example.themoviesapp.domain.model.MovieItem

class MovieAdapter(
    private var movieModels: List<MovieItem>,
    private val onClickListener: (idMovie: Int) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movieModels[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount() = movieModels.size
}