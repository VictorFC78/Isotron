package com.victor.isasturalmacen.screens.authentication

import DefaultDialogAlert
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.viewModels.authentication.ManageAccountViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import com.victor.isotronalmacen.components.DefaultTextField
import com.victor.isotronalmacen.components.DefaultToast


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAccountScreen(viewModel: ManageAccountViewModel = hiltViewModel(),
                        navigationToBack:()->Unit,
                        navigationToLogin:()->Unit,
                        navigateToDeleteUser:()->Unit,
                        navigateToHome:()->Unit){

    val user = ActualUser.getActualUser()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = user.name!!) }, actions = {
            IconButton(onClick = {viewModel.showDialogDownload()}) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_file_download_24),
                    contentDescription = ""
                )
            }
        })
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
            contentAlignment = Alignment.Center){

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
            Box(modifier = Modifier.fillMaxSize().padding(10.dp), contentAlignment = Alignment.BottomStart){
                FloatingActionButton(containerColor = Color.LightGray,
                    onClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, "Compartir con"))
                    }, modifier = Modifier.alpha(0.80f)
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            }
            DefaultDialogAlert(show = uiState.showConnectivityOk, dialogTitle = "SIN CONEXION",
                dialogText = "Revise la conexion,Si acepta cerrada la aplicacion",
                onConfirmation = {viewModel.closeApp()}, onDismissRequest = {})
            DialogInfoDownLoad(show = uiState.showDialogDownload,
                downLoadInOutProducts = {viewModel.downloadRegisterProductsByActualUser()},
                downLoadInOutTools = {viewModel.downloadRegisterToolsByActualUser()}) {
                viewModel.hideDialog()
            }
        }
    }

}
@Composable
fun DialogInfoDownLoad(show:Boolean,
                       downLoadInOutProducts:()->Unit,
                       downLoadInOutTools:()->Unit,
                       hideDialog:()->Unit) {
    if (show) {
        Dialog(onDismissRequest = { }) {

            Card(
                modifier = Modifier.size(width = 400.dp, height = 200.dp).alpha(0.75f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier.fillMaxSize()){
                    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                        Text(text = "DESGARGAR ARCHIVOS", textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold, color = Color.Black,
                            modifier = Modifier.padding(start = 20.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {hideDialog()}) {
                            Icon(Icons.Default.Close, contentDescription = "")
                        }
                    }
                    OutlinedButton(
                        onClick = { downLoadInOutTools() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                        )
                    ) {
                        Text("ENTRADAS/SALIDAS HERRAMIENTAS", color = Color.Black)
                    }
                    OutlinedButton(
                        onClick = { downLoadInOutProducts() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(

                        )
                    ) {
                        Text("ENTRADAS/SALIDAS PRODUCTOS", color = Color.Black)
                    }

                }

            }
        }
    }
}


