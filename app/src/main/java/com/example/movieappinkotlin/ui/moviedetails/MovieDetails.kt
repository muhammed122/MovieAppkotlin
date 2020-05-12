package com.example.movieappinkotlin.ui.moviedetails

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
import com.bumptech.glide.Glide
import com.example.movieappinkotlin.R
import com.example.movieappinkotlin.data.api.IMAGE_BASE_URl
import com.example.movieappinkotlin.data.model.movie_details.MovieDetailsResponse
import com.example.movieappinkotlin.data.repository.movie_details.MovieDetailRepository
import com.example.movieappinkotlin.data.connection.NetworkState
import kotlinx.android.synthetic.main.fragment_movie_details.*
import java.text.NumberFormat
import java.util.*
/**
 * A simple [Fragment] subclass.
 */
class MovieDetails : Fragment() {


    private lateinit var movieDetailRepository: MovieDetailRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        movieDetailRepository= MovieDetailRepository()

        val movie_id = arguments?.get("movie_id")


    //    Toast.makeText(requireContext(),""+movie_id,Toast.LENGTH_LONG).show()
         val viewModel: MovieDetailsViewModel  = getViewModel()

        viewModel.getMovieDetails(movie_id as Int)

        Log.e("here", "dola viewmodel " + viewModel)

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            addDataToView(it)

            Log.e("here", "dola " + it.title)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            Log.e("here", "dola " + it.status)

            progress_bar.visibility = if (it == NetworkState.Loading) View.VISIBLE
            else View.GONE
            txt_error.visibility = if (it == NetworkState.Error) View.VISIBLE
            else View.GONE

        })


    }


   private fun addDataToView(movie: MovieDetailsResponse) {
        movie_title.text = movie.title
        movie_tagline.text = movie.tagline
        movie_release_date.text = movie.releaseDate
        movie_rating.text = movie.vote_average.toString()
        movie_runtime.text = movie.runtime.toString() + " minutes"
        movie_overview.text = movie.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(movie.budget)
        movie_revenue.text = formatCurrency.format(movie.revenue)

        val moviePosterURL = IMAGE_BASE_URl + movie.poster_path
        Log.e("here", "dola " + moviePosterURL)

        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)
    }


    @Suppress("UNCHECKED_CAST")
    fun getViewModel(): MovieDetailsViewModel {

        return ViewModelProviders.of(requireActivity(), object : ViewModelProvider.Factory {

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                return MovieDetailsViewModel(movieDetailRepository) as T
            }
        })[MovieDetailsViewModel::class.java]


    }


}
