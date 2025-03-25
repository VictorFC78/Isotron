package com.victor.isasturalmacen.screens.authentication

import DefaultDialogAlert
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.viewModels.authentication.HomeViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import org.checkerframework.checker.units.qual.C

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()
               , navigateToBack:()->Unit,
               navigateToTools:()->Unit={},
               navigateToProducts:()->Unit={},
               navigateToManageAccount:()->Unit,
               navigateToLogout:()->Unit){
    val uiState by homeViewModel.uiState.collectAsState()
    homeViewModel.getUserName()

    Scaffold(topBar = {
        TopAppBar(title ={ Text(uiState.username) })
    },
        bottomBar = {
        DefaultBottomBarApp(
            navigateToBack = {homeViewModel.logOut() { navigateToBack() }},
            navigateToManageAccount = {navigateToManageAccount()},
            enableToHome = false, enableChangePassword = false
        ) {homeViewModel.logOut() { navigateToLogout() }
        }
    }) { paddingValues ->
        Box (modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.Black),
            contentAlignment = Alignment.TopStart){
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Image(painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
                modifier = Modifier.fillMaxWidth().padding(20.dp))
            Column(modifier = Modifier.fillMaxSize().padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.weight(1f))
                ItemSelected(R.drawable.portatiles, item = "HERRAMIENTAS") {
                    navigateToTools()
                }
                ItemSelected(R.drawable.productos, item = "PRODUCTOS") {
                    navigateToProducts()
                }
            }
            DefaultDialogAlert(show = uiState.showConnectivityAlert, dialogText = "Si Acepta Cerrará la Aplicacion manteniendo la sesion",
                dialogTitle = "SIN CONEXION", onDismissRequest = {homeViewModel.hideDialogAlert()}, onConfirmation = {homeViewModel.closeApp()})
        }
    }

}
@Composable
fun ItemSelected(image:Int,item:String,navigateTo:()->Unit){
   Box(modifier = Modifier.fillMaxWidth().height(160.dp).padding( vertical = 20.dp)
       .clickable { navigateTo() },
       contentAlignment = Alignment.Center){
       Image(painter = painterResource(image), contentDescription = "", modifier = Modifier.fillMaxWidth(),
           alpha = 0.55f, contentScale = ContentScale.FillWidth )
       Text("GESTION\n DE\n $item", fontSize = 20.sp, color = Color.Black,
           fontWeight = FontWeight.Bold, modifier = Modifier.padding().fillMaxWidth(),
           textAlign = TextAlign.Center, fontFamily = FontFamily.Serif)
   }
}