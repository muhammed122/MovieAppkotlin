package com.example.movieappinkotlin.data.repository.popular_movies

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieappinkotlin.data.api.MovieServices
import com.example.movieappinkotlin.data.model.popular_movies.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val context: Context,
    private val apiServices: MovieServices,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

     val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {

        val movieDataSource = MovieDataSource(context,apiServices, compositeDisposable)

        movieLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}