package com.example.movieappinkotlin.ui.popular_movies

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieappinkotlin.data.api.MOVIES_PER_PAGE
import com.example.movieappinkotlin.data.api.MovieServices
import com.example.movieappinkotlin.data.model.popular_movies.Movie
import com.example.movieappinkotlin.data.connection.NetworkState
import com.example.movieappinkotlin.data.repository.popular_movies.MovieDataSource
import com.example.movieappinkotlin.data.repository.popular_movies.MovieDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(val context: Context,
                              private val apiServices: MovieServices) {


    lateinit var moviePageList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory


    fun getMoviePagedList(compositeDisposable: CompositeDisposable):
            LiveData<PagedList<Movie>>{
        movieDataSourceFactory= MovieDataSourceFactory(context,apiServices,
            compositeDisposable)

        val config=PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(MOVIES_PER_PAGE)
            .build()


        moviePageList=LivePagedListBuilder(movieDataSourceFactory,config).build()

        return moviePageList
    }

    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.movieLiveDataSource,MovieDataSource::networkState
        )
    }

}