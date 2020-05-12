package com.example.movieappinkotlin.data.repository.movie_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieappinkotlin.data.api.ApiManager
import com.example.movieappinkotlin.data.model.movie_details.MovieDetailsResponse
import com.example.movieappinkotlin.data.connection.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import java.lang.Exception

class MovieNetworkDataSource(
                             private val compositeDisposable: CompositeDisposable){

private val _networkState=MutableLiveData<NetworkState>()
    val networkState:LiveData<NetworkState>
        get() = _networkState

private val _movieDetailsResponse=MutableLiveData<MovieDetailsResponse>()
    val movieDetailsResponse:LiveData<MovieDetailsResponse>
    get() = _movieDetailsResponse


fun getMovieDetails(movie_id:Int){

    _networkState.postValue(NetworkState.Loading)
    try {
        compositeDisposable.add(
            ApiManager.getService().getMovieDetails(movie_id)
                .subscribeOn(Schedulers.io())
                .subscribe({ it->
                    _movieDetailsResponse.postValue(it)
                    _networkState.postValue(NetworkState.Success)

                },{

                    _networkState.postValue(NetworkState.Error)
                    Log.e("here","error"+it.localizedMessage)
                }
                )
        )

    }
    catch (e:Exception){
        Log.e("here","error"+e.message)
    }


}
















}