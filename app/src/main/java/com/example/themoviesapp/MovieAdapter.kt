package com.example.themoviesapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(private var movies: List<Movie>, var activity: Activity): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val urlImage = "https://image.tmdb.org/t/p/w500"

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemMovieBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]

        Picasso.get().load(urlImage+item.poster_path).into(holder.binding.ivImageMain)
        holder.binding.tvTitleMain.setText(item.title)

        holder.binding.cvMovieContainer.setOnClickListener {
            var intent = Intent(activity, MovieDescriptionActivity::class.java)
            // Obtener la info de la pelicula mediante una consulta a la API o pasarle directamente los datos?
            //intent.putExtra("ID", item.id.toString())
            intent.putExtra("overView", item.overview)
            intent.putExtra("title", item.title)
            intent.putExtra("originalLanguage", item.original_language)
            intent.putExtra("popularity", item.popularity.toString())
            intent.putExtra("releaseDate", item.release_date)
            intent.putExtra("backdrop", item.backdrop_path)
            intent.putExtra("poster", item.poster_path)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int =  movies.size
}