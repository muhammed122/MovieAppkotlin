package com.example.movieappinkotlin.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieappinkotlin.data.model.popular_movies.Movie
import com.example.movieappinkotlin.data.connection.NetworkState
import com.example.movieappinkotlin.data.connection.NetworkStateMonitor
import io.reactivex.disposables.CompositeDisposable

class PopularMovieViewModel(
    private val networkStateMonitor: NetworkStateMonitor
    , private val moviePageListRepository: MoviePageListRepository
) :
    ViewModel() {


    fun getNetworkMonitorLiveData(): LiveData<NetworkState> =
        networkStateMonitor


    lateinit var compositeDisposable:CompositeDisposable

    lateinit var moviePageList: LiveData<PagedList<Movie>>

    fun getPopularMovies()
    {
        compositeDisposable= CompositeDisposable()
       moviePageList=moviePageListRepository.getMoviePagedList(compositeDisposable)
    }

   lateinit var networkState: LiveData<NetworkState>
     fun getNetworkState(){
     networkState= moviePageListRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePageList.value?.isEmpty() ?: true
    }

    fun refreshData(){
        moviePageList.value?.dataSource?.invalidate()

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}