package com.example.movieappinkotlin.data.repository.movie_details
import androidx.lifecycle.LiveData
import com.example.movieappinkotlin.data.model.movie_details.MovieDetailsResponse
import com.example.movieappinkotlin.data.connection.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository {

    lateinit var movieNetworkDataSource: MovieNetworkDataSource

    fun getSingleMovieDetails(compositeDisposable: CompositeDisposable, movie_id:Int)
            :LiveData<MovieDetailsResponse>{

        movieNetworkDataSource=
            MovieNetworkDataSource(
                compositeDisposable
            )
        movieNetworkDataSource.getMovieDetails(movie_id)

        return movieNetworkDataSource.movieDetailsResponse

    }

    fun getNetworkState():LiveData<NetworkState>{
        return movieNetworkDataSource.networkState
    }
}