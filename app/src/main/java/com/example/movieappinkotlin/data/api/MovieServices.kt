package com.example.movieappinkotlin.data.api

import com.example.movieappinkotlin.data.model.movie_details.MovieDetailsResponse
import com.example.movieappinkotlin.data.model.popular_movies.PopularMovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieServices {



    @GET("movie/popular")
    fun getPopularMovies(@Query("page")page:Int):Single<PopularMovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movie_id:Int):Single<MovieDetailsResponse>



}