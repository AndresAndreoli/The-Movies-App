package com.example.themoviesapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

// Clase que implemento para serializar datos de un String a una Lista de enteros y viceversa, que es utilizado en la base de datos.
class Converters {
    @TypeConverter
    fun fromString(value: String?): List<Int?>? {
        val listType: Type = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Int?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}