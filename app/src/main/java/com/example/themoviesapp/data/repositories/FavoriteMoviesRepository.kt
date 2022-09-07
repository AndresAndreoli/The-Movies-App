package com.example.themoviesapp.data.repositories

import android.util.Log
import com.example.themoviesapp.data.Cache
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class FavoriteMoviesRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val moviesCache: Cache
) {
     suspend fun retrieveIDFavoriteMoviesFromFirebase(){
        var IDMovies = HashSet<Int>()

        firebaseFirestore.collection("favoriteMovies")
            .get()
            .addOnSuccessListener { documents ->
                documents.map{ IDMovies.add(it.data.get("ID").toString().toInt()) }
                //Log.d("idMovie", "${document.id} => ${document.data}")
            }
            .addOnFailureListener { exception ->
                Log.w("Error FireStore", "Error getting documents: ", exception)
            }

        // Load cache
        moviesCache.favoriteMovies.addAll(IDMovies)
    }
}