package com.victor.isasturalmacen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.navigation.NavigationWrapper
import com.victor.isasturalmacen.ui.theme.IsasturAlmacenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = baseContext
            Connectivity(context).activateConnectivity()
            NavigationWrapper()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }
}
