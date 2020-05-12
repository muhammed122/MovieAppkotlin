package com.example.movieappinkotlin.data.connection

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData


class NetworkStateMonitor


constructor(private val cm:ConnectivityManager ):LiveData<NetworkState>() {
    private var hasNetworkChanged: Boolean = false

    private val networkStateObject = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            hasNetworkChanged = true
            postValue(NetworkState.CONNECTION_LOST)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            hasNetworkChanged = true
            postValue(NetworkState.DISCONNECTED)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if(hasNetworkChanged) {
                postValue(NetworkState.CONNECTED)
            }
        }
    }
    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(networkStateObject)
    }

    override fun onActive() {
        super.onActive()
        cm.registerNetworkCallback(networkRequestBuilder(), networkStateObject)
    }

    private fun networkRequestBuilder(): NetworkRequest {
        return NetworkRequest.Builder()
            
            .build()
    }

}