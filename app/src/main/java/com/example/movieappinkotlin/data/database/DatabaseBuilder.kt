package com.example.movieappinkotlin.data.database

import android.content.Context
import androidx.room.Room

class DatabaseBuilder {

    @Volatile
    private var movieDatabase: MovieDatabase? = null

    private val DB_NAME = "Movies"

    fun getInstance(context: Context): MovieDatabase {
        if (movieDatabase == null) {
            synchronized(MovieDatabase::class.java) {
                if (movieDatabase == null) {
                    movieDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java, DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return movieDatabase!!
    }
}