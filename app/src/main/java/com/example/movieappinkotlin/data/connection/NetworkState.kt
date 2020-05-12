package com.example.movieappinkotlin.data.connection

class NetworkState(val status: Status, val masssage:String ) {

    companion object{

        val Loading: NetworkState =
            NetworkState(
                Status.RUNNING,
                "Loading"
            )
        val Success: NetworkState =
            NetworkState(
                Status.SUCCESS,
                "success"
            )
        val Error: NetworkState =
            NetworkState(
                Status.FAILED,
                "something wrong"
            )
        val NOMORE: NetworkState =
            NetworkState(
                Status.FAILED,
                "No More movies"
            )

        val CONNECTION_LOST: NetworkState =
            NetworkState(
                Status.CONNECTION_LOST,
                "Connection Loss"
            )
        val DISCONNECTED: NetworkState =
            NetworkState(
                Status.DISCONNECTED,
                "Disconnection"
            )
        val CONNECTED: NetworkState =
            NetworkState(
                Status.CONNECTED,
                "Connected"
            )


    }
}