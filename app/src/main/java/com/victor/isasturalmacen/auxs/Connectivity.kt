package com.victor.isasturalmacen.auxs

import android.content.Context
import android.net.ConnectivityManager

class Connectivity(val context: Context) {
    val connectivity:ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    fun activateConnectivity(){
        connect = connectivity
        comContext = context
    }
    companion object{
        private lateinit var connect :ConnectivityManager
        private lateinit var comContext:Context
        fun connectOk(): Boolean? {
            return connect.activeNetworkInfo?.isConnected
        }
        fun getContext() = comContext

    }
}