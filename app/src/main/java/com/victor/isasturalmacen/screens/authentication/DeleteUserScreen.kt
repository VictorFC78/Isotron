package com.victor.isasturalmacen.screens.authentication

import DefaultDialogAlert
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.viewModels.authentication.DeleteUserViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import com.victor.isotronalmacen.components.DefaultCircularProgressBar
import com.victor.isotronalmacen.components.DefaultTextField
import com.victor.isotronalmacen.components.DefaultsTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteUserScreen(viewModel: DeleteUserViewModel = hiltViewModel(),
                     navigateToToBack:()->Unit,
                     navigateToHome:()->Unit,
                     navigateToLoginScreen:()->Unit){

    val uiState  by viewModel.uiState.collectAsState()

    Scaffold (topBar = {
        DefaultsTopAppBar(backgroundColor = Color.LightGray,enableButtons = false, title = uiState.username)
    }, bottomBar = {
        DefaultBottomBarApp(navigateToBack = {navigateToToBack()}, enableChangePassword = false,
           enableManageAccount = false, enableDeleteUser = false,
            navigateToHome ={ navigateToHome()}) {
                navigateToLoginScreen()
        }
    }){ paddingValues ->
        DefaultCircularProgressBar(uiState.showCircularBar)
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.TopStart){
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Image(painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
                modifier = Modifier.fillMaxWidth().padding(20.dp))
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(1f))
                Card(modifier = Modifier.fillMaxWidth().height(200.dp).alpha(0.75f),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                        Text(modifier = Modifier.fillMaxWidth(), text = "INTRODUZCA EL CORREO DE SU CUENTA")
                        DefaultTextField(modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 20.dp),
                            value =uiState.emailActualUser,
                            onValueChange = {viewModel.onValuesChanged(it)},
                            label = "Email", icon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email)

                        Button(onClick = { viewModel.deleteActualUser { navigateToLoginScreen() }}
                            , modifier = Modifier.fillMaxWidth()
                                .height(50.dp), enabled = uiState.enableButton
                            , shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(
                                0xFF0B6FBE))) {
                            Text("ELIMINAR CUENTA", fontSize = 26.sp)
                        }
                    }
                }
            }
            DefaultDialogAlert(show=uiState.showDialogAlert, onDismissRequest = {viewModel.hideDialogAlert()},
                onConfirmation = {viewModel.hideDialogAlert()}, dialogTitle = "ERROR", dialogText = "Revise la conexion/email")
        }

    }
}
