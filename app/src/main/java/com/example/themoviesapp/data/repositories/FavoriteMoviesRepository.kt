package com.example.themoviesapp.data.repositories

import android.util.Log
import com.example.themoviesapp.data.Cache
import com.example.themoviesapp.data.database.dao.MovieDao
import com.example.themoviesapp.data.services.MovieDetailsService
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.entities.MovieEntity
import com.example.themoviesapp.utils.toDataBase
import com.example.themoviesapp.utils.toDomain
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class FavoriteMoviesRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val moviesDao: MovieDao,
    private val moviesCache: Cache,
    private val moviesDetailsService: MovieDetailsService,
) {
     suspend fun retrieveIDFavoriteMoviesFromFirebase(){
        var IDMovies = HashMap<Int, String>()

        firebaseFirestore.collection("favoriteMovies")
            .get()
            .addOnSuccessListener { documents ->
                documents.map{
                    IDMovies.put(
                        it.data.get("ID").toString().toInt(), // Movie ID
                        it.id // Firestore hash code
                    )
                }
                //Log.d("idMovie", "${document.id} => ${document.data}")

                // Load cache
                moviesCache.favoriteMovies.putAll(IDMovies)
            }
            .addOnFailureListener { exception ->
                Log.w("Error FireStore", "Error getting documents: ", exception)
            }
    }

    suspend fun getFavoriteMoviesFromDB(): List<MovieItem> {
        // mapeo el modelo de datos de ModelEntity a Model
        val response: List<MovieEntity> = moviesDao.getAllFavoriteMovies()

        return response.map { it.toDomain() }
    }

    // This function optimizes the data load, since fill the DB with movies already searched before, otherwise, fetch on api
    suspend fun loadDBWithFavoriteMovies() {
        var IDsFromDB: HashSet<Int> = moviesDao.recoverIDs().toHashSet()
        moviesCache.favoriteMovies.forEach {
            if (!IDsFromDB.contains(it.key)) {
                var result: MovieEntity

                when {
                    moviesCache.popularMovie.containsKey(it.key) ->{
                        // Recover favorite movie from CACHE - popularMovie
                        result = moviesCache.popularMovie.get(it.key)!!.toDataBase()
                    }
                    moviesCache.upcomingMovie.containsKey(it.key) ->{
                        // Recover favorite movie from CACHE - upcomingMovie
                        result = moviesCache.upcomingMovie.get(it.key)!!.toDataBase()
                    }
                    moviesCache.topRatedMovie.containsKey(it.key) ->{
                        // Recover favorite movie from CACHE - topRatedMovie
                        result = moviesCache.topRatedMovie.get(it.key)!!.toDataBase()
                    }
                    else -> {
                        // Recover favorite movie from API
                        result = moviesDetailsService.getMovieDetailsResponse(it.key).data.toDataBase()
                    }
                }
                moviesDao.insertMovie(result)
            }
        }
    }

    suspend fun insertFavoriteMovieToDB(movie: MovieItem) {
        val convertMovie = movie.toDataBase()
        // Insert on DB
        moviesDao.insertMovie(convertMovie)

        // Insert on Firestore
        firebaseFirestore.collection("favoriteMovies")
            .add(
                hashMapOf(
                    "ID" to movie.id
                )
            )
            .addOnSuccessListener {
                // TODO: evento analitys

                // Insert movie ID on Cache
                if (!moviesCache.favoriteMovies.contains(movie.id)) {
                    moviesCache.favoriteMovies.put(movie.id!!, it.id)
                }

            }
            .addOnFailureListener {
                //TODO: evento analitys
            }
    }

    suspend fun deleteFavoriteMovieFromDB(idMovie: Int) {
        // Delete from DB
        moviesDao.removeMovieFromDB(idMovie)

        // Delete from Firestore
        var idDocument = moviesCache.favoriteMovies.get(idMovie)
        firebaseFirestore.collection("favoriteMovies")
            .document(idDocument ?: "000aaa")
            .delete()
            .addOnSuccessListener {
                // TODO: evento analitys

                // Delete favorite movie ID on Cache
                if (moviesCache.favoriteMovies.contains(idMovie)) {
                    moviesCache.favoriteMovies.remove(idMovie)
                }
            }
            .addOnFailureListener {
                //TODO: evento analitys
            }
    }

    suspend fun clearDB(){
        moviesDao.clearDB()
    }
}