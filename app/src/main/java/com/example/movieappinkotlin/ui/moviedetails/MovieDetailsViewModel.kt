package com.example.movieappinkotlin.ui.moviedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieappinkotlin.data.model.movie_details.MovieDetailsResponse
import com.example.movieappinkotlin.data.repository.movie_details.MovieDetailRepository
import com.example.movieappinkotlin.data.connection.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel
    (private val movieDetailRepository: MovieDetailRepository)
    : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

   lateinit var  movieDetails : LiveData<MovieDetailsResponse>
   lateinit var  networkState : LiveData<NetworkState>

    fun getMovieDetails(movie_id:Int){
        movieDetails=movieDetailRepository.getSingleMovieDetails(compositeDisposable,movie_id)
        networkState=movieDetailRepository.getNetworkState()

    }
//    val movieDetails: LiveData<MovieDetailsResponse> by lazy {
//        movieDetailRepository.getSingleMovieDetails(compositeDisposable, movie_id)
//    }

//    val networkState: LiveData<NetworkState> by lazy {
//        movieDetailRepository.getNetworkState()
//    }




    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        Log.e("here","dola finished")
    }


}