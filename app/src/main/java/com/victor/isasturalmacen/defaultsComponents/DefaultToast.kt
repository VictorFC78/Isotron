package com.victor.isotronalmacen.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.victor.isasturalmacen.auxs.Connectivity

@Composable
fun DefaultToast(
    message:String){
        Toast.makeText(Connectivity.getContext(),message,Toast.LENGTH_LONG).show()

}