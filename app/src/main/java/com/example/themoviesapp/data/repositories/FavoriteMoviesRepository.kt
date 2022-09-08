package com.example.themoviesapp.data.repositories

import android.util.Log
import com.example.themoviesapp.data.Cache
import com.example.themoviesapp.data.database.dao.MovieDao
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class FavoriteMoviesRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val moviesDao: MovieDao,
    private val moviesCache: Cache
) {
     suspend fun retrieveIDFavoriteMoviesFromFirebase(){
        var IDMovies = HashMap<Int, String>()

        firebaseFirestore.collection("favoriteMovies")
            .get()
            .addOnSuccessListener { documents ->
                documents.map{
                    IDMovies.put(
                        it.data.get("ID").toString().toInt(),
                        it.id
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

    suspend fun clearDB(){
        moviesDao.clearDB()
    }
}