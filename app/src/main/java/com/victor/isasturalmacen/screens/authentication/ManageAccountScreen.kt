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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.viewModels.authentication.ManageAccountViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import com.victor.isotronalmacen.components.DefaultTextField
import com.victor.isotronalmacen.components.DefaultToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAccountScreen(viewModel: ManageAccountViewModel = hiltViewModel(),
                        navigationToBack:()->Unit,
                        navigationToLogin:()->Unit,
                        navigateToDeleteUser:()->Unit,
                        navigateToHome:()->Unit){

    val user = ActualUser.getActualUser()
    val uiState by viewModel.manageAccountUiState.collectAsState()


    Scaffold(topBar = {
        TopAppBar(title = { Text(text = user.name!!) })
    }, bottomBar = {
        DefaultBottomBarApp(navigateToBack = {navigationToBack()},
                            navigateToHome = {navigateToHome()},
                            onClickChangePassword = {viewModel.onClickChangePassword()},
                            enableManageAccount = false,
                            enableDeleteUser = true,
                            navigateToDeleteUser = {navigateToDeleteUser()}) {
           viewModel.logOut() { navigationToLogin() }
        }
    }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.Black),
            contentAlignment = Alignment.TopStart){
            if(uiState.showToast){
                DefaultToast(uiState.messageToast)
                viewModel.hideToastAndDialog()
            }
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Image(painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
                modifier = Modifier.fillMaxWidth().padding(20.dp))
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(1f))
                if(uiState.showInputData){
                    Card (modifier = Modifier.fillMaxWidth().height(280.dp).alpha(0.75f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp,)
                    ){
                        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                            Text("CAMBIO DE CONTRASEÑA", modifier = Modifier.fillMaxWidth(),
                                color=Color.White, fontWeight = FontWeight.Bold)
                            DefaultTextField(modifier = Modifier.fillMaxWidth().padding(top=20.dp,bottom = 20.dp),
                                value = uiState.newPassword, label = "Nueva Contraseña",
                                icon = Icons.Filled.Lock, keyboardType = KeyboardType.Password,
                                visualTransformation = PasswordVisualTransformation(),
                                onValueChange = {viewModel.onAllValuesChanged(it,uiState.repeatNewPassword)})
                            DefaultTextField(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                                value = uiState.repeatNewPassword, label = "Repita Contraseña",
                                icon = Icons.Filled.Lock, keyboardType = KeyboardType.Password,
                                visualTransformation = PasswordVisualTransformation(),
                                onValueChange = {viewModel.onAllValuesChanged(uiState.newPassword,it)})
                            Button(onClick = {viewModel.changePassword(uiState.newPassword)}
                                , modifier = Modifier.fillMaxWidth()
                                    .height(60.dp), enabled = uiState.formIsValid
                                , shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(
                                    0xFF0B6FBE))) {
                                Text("CAMBIAR CONTRASEÑA", fontSize = 26.sp)
                            }
                        }

                    }
                }
            }
            DefaultDialogAlert(show = uiState.showConnectivityOk, dialogTitle = "SIN CONEXION",
                dialogText = "Revise la conexion,Si acepta cerrada la aplicacion",
                onConfirmation = {viewModel.closeApp()}, onDismissRequest = {})
        }
    }

}



