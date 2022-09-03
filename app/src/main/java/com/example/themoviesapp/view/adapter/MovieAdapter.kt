package com.example.themoviesapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themoviesapp.R
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.example.themoviesapp.model.movieResponse.MovieModel
import com.example.themoviesapp.data.services.APIService

class MovieAdapter(
    private var movieModels: List<MovieModel>,
    private val onClickListener: (idMovie: Int) -> Unit
    ): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemMovieBinding.bind(itemView)

        fun render (
            movieModel: MovieModel,
            onClickListener: (idMovie: Int) -> Unit
        ){
            Glide.with(binding.cvMovieContainer.context)
                .load(APIService.urlImage+movieModel.poster_path)
                .into(binding.ivImageMain)
            binding.tvTitleMain.setText(movieModel.title)

            binding.cvMovieContainer.setOnClickListener {
                onClickListener(movieModel.id ?: -1)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movieModels[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return movieModels.size
    }
}