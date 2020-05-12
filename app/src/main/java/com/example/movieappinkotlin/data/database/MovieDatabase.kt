package com.example.movieappinkotlin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieappinkotlin.data.model.popular_movies.Movie

@Database(entities = [Movie::class],version = 4)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao():MovieDao



}