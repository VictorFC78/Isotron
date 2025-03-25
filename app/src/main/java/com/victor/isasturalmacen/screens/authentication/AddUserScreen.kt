package com.victor.isasturalmacen.screens.authentication

import DefaultDialogAlert
import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.viewModels.authentication.AddUserViewModel
import com.victor.isotronalmacen.components.DefaultTextField
import com.victor.isotronalmacen.components.DefaultToast


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun AddUserScreen(viewModel: AddUserViewModel = hiltViewModel(),
                  navigateToBack:()->Unit,
                  navigateToHome:()->Unit){

    val uiState by viewModel.uiState.collectAsState()

        Scaffold(topBar = {
            TopAppBar(title = { Text("Inserte sus datos") },
                navigationIcon ={
                    IconButton(onClick = {navigateToBack()}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                } )
        }

        ) {paddingValues ->

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues )
            .background(Color.Black), contentAlignment = Alignment.TopStart){
            if(uiState.showToast){
                DefaultToast(uiState.messageToast)
                viewModel.hideDialogAndToast()
            }
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Image(painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
                modifier = Modifier.fillMaxWidth().padding(20.dp))
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier=Modifier.weight(1f))
                Card (modifier = Modifier.fillMaxWidth().height(550.dp).alpha(0.75f),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp) ,
                    colors = CardDefaults.cardColors(containerColor = Color.White)){
                    Column(modifier = Modifier.fillMaxSize().padding(20.dp,30.dp)) {
                        DefaultTextField(modifier = Modifier.fillMaxWidth(), value =uiState.email,
                            onValueChange = {viewModel.onAllValueChanged(email = it, password = uiState.password,
                                name = uiState.name, job = uiState.job, repitPassword = uiState.repeatPassword)},
                            label = "Email", icon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email)
                        DefaultTextField(modifier = Modifier.fillMaxWidth()
                            .padding(top=20.dp, bottom = 10.dp), value =uiState.password,
                            onValueChange = {viewModel.onAllValueChanged(email = uiState.email, password = it,
                                name = uiState.name, job = uiState.job, repitPassword = uiState.repeatPassword)},
                            label = "Password", icon = Icons.Default.Lock,
                            keyboardType = KeyboardType.Password,
                            visualTransformation = PasswordVisualTransformation())
                        DefaultTextField(modifier = Modifier.fillMaxWidth()
                            .padding(top=20.dp, bottom = 10.dp), value =uiState.repeatPassword,
                            onValueChange = {viewModel.onAllValueChanged(uiState.email,uiState.password,uiState.name,uiState.job,it)},
                            label = "Repita Password", icon = Icons.Default.Lock,
                            keyboardType = KeyboardType.Password,
                            visualTransformation = PasswordVisualTransformation())
                        DefaultTextField(modifier = Modifier.fillMaxWidth()
                            .padding(top=20.dp, bottom = 10.dp), value =uiState.name,
                            onValueChange = {viewModel.onAllValueChanged(uiState.email,uiState.password,it,uiState.job,uiState.repeatPassword)},
                            label = "Nombre", icon = Icons.Default.Create)
                        DefaultTextField(modifier = Modifier.fillMaxWidth()
                            .padding(top=20.dp, bottom = 10.dp), value =uiState.job,
                            onValueChange = {viewModel.onAllValueChanged(uiState.email,uiState.password,uiState.name,it,uiState.repeatPassword)},
                            label = "Puesto", icon = Icons.Default.Create)
                        Button(onClick = {viewModel.createUserWithEmailAndPassword { navigateToHome() } }
                        , modifier = Modifier.fillMaxWidth()
                            .height(60.dp).padding(top=10.dp), enabled = uiState.enable
                            , shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(
                                0xFF0B6FBE))) {
                            Text("REGISTRARSE", fontSize = 26.sp)
                        }
                    }
                }
            }
            DefaultDialogAlert(show = uiState.showDialogConnectivity, dialogTitle = stringResource(R.string.sin_con),
                dialogText = stringResource(R.string.rev_con), onConfirmation = {viewModel.hideDialogAndToast()},
                onDismissRequest = {})
        }
    }
}
