package com.example.movieappinkotlin.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieappinkotlin.data.model.popular_movies.Movie

@Dao
interface MovieDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE )
   fun insertMovies(movies:List<Movie>)

    @Query("select * from Movie")
    fun getAllMovies():List<Movie>
}