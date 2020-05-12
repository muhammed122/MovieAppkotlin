package com.example.movieappinkotlin.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieappinkotlin.R

class MainActivity : AppCompatActivity() {

    private lateinit var connectivityManager: ConnectivityManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest: NetworkRequest = NetworkRequest.Builder().build()
       // connectivityManager.registerNetworkCallback(networkRequest, ConnectivityCallback())


    }



}
