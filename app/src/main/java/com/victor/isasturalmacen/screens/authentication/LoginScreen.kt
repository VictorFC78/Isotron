package com.victor.isasturalmacen.screens.authentication


import DefaultDialogAlert
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.viewModels.authentication.LoginViewModel
import com.victor.isotronalmacen.components.DefaultCircularProgressBar
import com.victor.isotronalmacen.components.DefaultTextField



@SuppressLint("ResourceAsColor")
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(),
                navigateToHome:()->Unit,
                navigateToAddUser:()->Unit){

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        contentAlignment = Alignment.TopStart){
        Image(painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, start = 20.dp, end = 20.dp))

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                , shape =  RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp) ,
                colors =CardDefaults.cardColors(containerColor = Color(0xFF6B6868))
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 30.dp)) {
                    DefaultTextField(modifier = Modifier.fillMaxWidth(), value = uiState.email,
                        onValueChange = {viewModel.onChangedEmailAndPasswordValues(it,uiState.password)},
                        label = "Email", icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email)

                    DefaultTextField(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp), value = uiState.password,
                        onValueChange = {viewModel.onChangedEmailAndPasswordValues(uiState.email,it)},
                        label = "Password", icon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        visualTransformation = PasswordVisualTransformation())

                    Button(onClick = {viewModel.login(uiState.email,uiState.password) { navigateToHome() }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp), enabled = uiState.enable
                        , shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(R.color.azul_iso))) {
                        Text("LOGIN", fontSize = 26.sp)
                    }
                    Text(text= stringResource(R.string.registrarse),
                        fontSize = 16.sp,
                       modifier = Modifier
                           .fillMaxSize()
                           .padding(top = 20.dp)
                           .clickable { navigateToAddUser() },
                        color = Color(R.color.orange), textAlign = TextAlign.Center)
                }

            }
        }
        DefaultDialogAlert(show = uiState.show, dialogTitle = stringResource(R.string.error_login),
            dialogText = stringResource(R.string.rev_email_contr),
            onDismissRequest = {}, onConfirmation = {viewModel.hideAlertDialog()})
        DefaultDialogAlert(show = uiState.showConnectivityAlert, dialogText = stringResource(R.string.rev_con),
            dialogTitle = stringResource(R.string.sin_con), onConfirmation ={viewModel.hideAlertDialog()} , onDismissRequest = {})
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(), verticalArrangement = Arrangement.Center) {
            DefaultCircularProgressBar(show = uiState.showCircularProgressBar)
        }

    }
}
