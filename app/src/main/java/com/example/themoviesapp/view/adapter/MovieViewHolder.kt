package com.example.themoviesapp.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themoviesapp.data.services.APIService
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.example.themoviesapp.domain.model.MovieItem

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemMovieBinding.bind(itemView)

    fun render(
        movieModel: MovieItem,
        onClickListener: (idMovie: Int) -> Unit
    ) {
        Glide.with(binding.cvMovieContainer.context)
            .load(APIService.urlImage + movieModel.poster_path)
            .into(binding.ivImageMain)
        binding.tvTitleMain.setText(movieModel.title)

        binding.cvMovieContainer.setOnClickListener {
            onClickListener(movieModel.id ?: -1)
        }
    }
}