package com.victor.isasturalmacen.defaultsComponents

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable

@Composable
fun ToastDefault(context: Context,message:String){

    Toast.makeText(context,message,Toast.LENGTH_LONG).show()
}