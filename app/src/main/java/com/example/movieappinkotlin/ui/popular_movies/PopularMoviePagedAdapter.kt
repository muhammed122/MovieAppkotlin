package com.example.movieappinkotlin.ui.popular_movies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappinkotlin.R
import com.example.movieappinkotlin.data.api.IMAGE_BASE_URl
import com.example.movieappinkotlin.data.model.popular_movies.Movie
import com.example.movieappinkotlin.data.connection.NetworkState
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*


class PopularMoviePagedAdapter(private val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIEW_VIEW=1
    val NETWORK_VIEW=2

    private var networkState: NetworkState?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View

        if (viewType==MOVIEW_VIEW) {
            view = layoutInflater.inflate(R.layout.movie_item, parent, false)
            return MovieViewHolder(view)
        }
        else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateHolder(view)
        }



    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.Loading
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW
        } else {
            MOVIEW_VIEW
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position)==MOVIEW_VIEW){
            (holder as MovieViewHolder).bind(getItem(position),context)
        }
        else
            (holder as NetworkStateHolder).bind(networkState)

    }


    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie?, context: Context) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = movie?.releaseDate

            val moviePosterURL = IMAGE_BASE_URl + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster)

            itemView.setOnClickListener {
                val data = Bundle()
             //   data.putString("movie_id", movie?.id.toString())
                data.putInt("movie_id",movie?.id as Int)
                itemView.findNavController().navigate(
                    R.id.action_popularMovies_to_movieDetails,
                    data
                )
            }


        }


    }

    class NetworkStateHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){

            if (networkState!=null && networkState== NetworkState.Loading)
                itemView.progress_bar_item.visibility=View.VISIBLE
            else
                itemView.progress_bar_item.visibility=View.GONE

        }

    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }
}


