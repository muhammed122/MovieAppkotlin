package com.example.movieappinkotlin.ui.popular_movies


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieappinkotlin.R
import com.example.movieappinkotlin.data.api.ApiManager
import com.example.movieappinkotlin.data.api.MovieServices
import com.example.movieappinkotlin.data.connection.NetworkState
import com.example.movieappinkotlin.data.connection.NetworkStateMonitor
import com.example.movieappinkotlin.data.repository.popular_movies.MovieDataSource
import kotlinx.android.synthetic.main.fragment_popular_movies.*

/**
 * A simple [Fragment] subclass.
 */
class PopularMovies : Fragment() {

    private lateinit var viewModel: PopularMovieViewModel

    lateinit var moviePagedListRepository: MoviePageListRepository

    lateinit var  movieAdapter:PopularMoviePagedAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        intiRecycler()


        val apiService : MovieServices = ApiManager.getService()
        moviePagedListRepository = MoviePageListRepository(requireContext(),apiService)


        viewModel = getViewModel()
        viewModel.getPopularMovies()
        viewModel.getNetworkState()

        viewModel.moviePageList.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)

            Log.e("heree","dola "+it.size)


        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.Loading) View.VISIBLE else View.GONE
            //txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.Success) View.VISIBLE else View.GONE
            connection_layout.visibility = if (it== NetworkState.Error) View.VISIBLE else View.GONE


            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })


        txt_retry.setOnClickListener {
            viewModel.refreshData()
        }


        setupNetworkObserver()
    }


    private fun intiRecycler(){

        movieAdapter = PopularMoviePagedAdapter(requireContext())
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIEW_VIEW) return  1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }

        popularMovies_recycler.layoutManager = gridLayoutManager
        popularMovies_recycler.setHasFixedSize(true)
        popularMovies_recycler.adapter = movieAdapter
    }

    private fun getViewModel(): PopularMovieViewModel {
        val cm:ConnectivityManager= context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularMovieViewModel(NetworkStateMonitor(cm),moviePagedListRepository) as T
            }
        })[PopularMovieViewModel::class.java]
    }
    private fun setupNetworkObserver() {
        viewModel.getNetworkMonitorLiveData().observe(viewLifecycleOwner,
            Observer {
            when (it) {
                NetworkState.CONNECTED -> {

                    viewModel.refreshData()
                    Toast.makeText(requireContext(),
                        ""+NetworkState.CONNECTED.masssage,Toast.LENGTH_LONG).show()
//                    viewModel.getPopularMovies()
                     viewModel.getNetworkState()

                }
                NetworkState.CONNECTION_LOST -> {
                   Toast.makeText(requireContext(),
                       ""+NetworkState.CONNECTION_LOST.masssage,Toast.LENGTH_LONG).show()

                }
                NetworkState.DISCONNECTED -> {
                    Log.e("heree","dola disconnected")
                   // connection_layout.visibility =  View.VISIBLE



                }
                else -> {

                }
            }
        })
    }

}
