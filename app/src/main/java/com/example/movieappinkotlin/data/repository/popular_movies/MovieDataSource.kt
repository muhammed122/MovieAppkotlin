package com.example.movieappinkotlin.data.repository.popular_movies

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieappinkotlin.data.api.FIRST_PAGE
import com.example.movieappinkotlin.data.api.MovieServices
import com.example.movieappinkotlin.data.database.DatabaseBuilder
import com.example.movieappinkotlin.data.model.popular_movies.Movie
import com.example.movieappinkotlin.data.connection.NetworkState
import com.example.movieappinkotlin.data.connection.NetworkStateMonitor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val context: Context,
    private val apiServices: MovieServices, private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {


    val cm:ConnectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val connection=NetworkStateMonitor(cm)

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    )
    {
        networkState.postValue(NetworkState.Loading)
        compositeDisposable.add(
            apiServices.getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .subscribe({ t1 ->
                    callback.onResult(t1.results!!, null, page + 1)
                    networkState.postValue(NetworkState.Success)

                    //  cacheMovies(t1.results as List<Movie>)

                    Log.e("here", "dola" + t1.page)
                },
                    { t2 ->
                        networkState.postValue(NetworkState.Error)
//
//                        val movies = getCachedMovies()
//                        callback.onResult(movies, null, page+1)
//

                        Log.e("here", "dola" + t2.localizedMessage)
                    }

                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.Loading)
        compositeDisposable.add(apiServices.getPopularMovies(params.key)
            .subscribeOn(Schedulers.io())
            .subscribe({ t1 ->
                if (t1.totalPages!! > params.key) {
                    callback.onResult(t1.results!!, params.key + 1)
                    networkState.postValue(NetworkState.Success)
                    // cacheMovies(t1.results as List<Movie>)
                } else {

                    networkState.postValue(NetworkState.NOMORE)
                }
            },
                { t2 ->
                    //                    val movies = getCachedMovies()
//                    callback.onResult(movies, params.key+1)
                    networkState.postValue(NetworkState.Error)
                    Log.e("here", "" + t2.localizedMessage)

                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun cacheMovies(movies: List<Movie>) {
        Thread {

            DatabaseBuilder().getInstance(context).getMovieDao().insertMovies(movies)
        }.start()

    }

    fun getCachedMovies(): List<Movie> {

        var data: List<Movie>? = null

        Thread {
            data = DatabaseBuilder().getInstance(context).getMovieDao().getAllMovies()
        }.start()


        Log.e("here", "dola size" + data!!.size)
        return data!!
    }
}