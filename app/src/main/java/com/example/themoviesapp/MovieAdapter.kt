package com.example.themoviesapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviesapp.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(private var movies: List<Movie>, var activity: Activity): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemMovieBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]

        Picasso.get().load(APIService.urlImage+item.poster_path).into(holder.binding.ivImageMain)
        holder.binding.tvTitleMain.setText(item.title)

        holder.binding.cvMovieContainer.setOnClickListener {
            var intent = Intent(activity, MovieDescriptionActivity::class.java)
            intent.putExtra("ID", item.id.toString())
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}