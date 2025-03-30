package com.victor.isasturalmacen.screens.tools

import DefaultDialogAlert
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.viewModels.tools.AddToolViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import com.victor.isotronalmacen.components.DefaultTextField

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewToolScreen(viewModel: AddToolViewModel = hiltViewModel(),
                     navigateToBack:()->Unit,
                     navigateToHome:()->Unit,
                     navigateToManageAccount:()->Unit,
                     navigateToLogout:()->Unit){

    val uiState by viewModel.uiState.collectAsState()


    Scaffold(topBar = {
        TopAppBar(title = {Text(uiState.userName)})
    },
        bottomBar = {
            DefaultBottomBarApp(navigateToBack ={navigateToBack()},
                                navigateToHome={navigateToHome()},
                                navigateToManageAccount = {navigateToManageAccount()},
                                enableChangePassword = false) { navigateToLogout() }
        }) { paddingValues ->
        Box (modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.Black),
            contentAlignment = Alignment.TopEnd,){
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Image(painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
                modifier = Modifier.fillMaxWidth().padding(20.dp))
           DialogIdTool (show = uiState.showHelpDialog){
               viewModel.hideDialogs()
           }
            DialogExitNewTool(uiState.showExitDialog) {
                viewModel.hideDialogs()
            }
            DefaultDialogAlert(show = uiState.showErrorDialog, onConfirmation = {viewModel.hideDialogs()},
                onDismissRequest = {viewModel.hideDialogs()}, dialogTitle = "ERROR", dialogText = "se ha producido un error al crear la herramienta")
            DefaultDialogAlert(show = uiState.showConnectivityOk, dialogTitle = "SIN CONEXION", dialogText = "Sin conexion no se pueden añadir herramientas",
                onConfirmation = {viewModel.hideDialogs()}, onDismissRequest = {viewModel.hideDialogs()})
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))
            Card (modifier = Modifier.fillMaxWidth().height(300.dp).alpha(0.75f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ){
                Row(modifier = Modifier.fillMaxWidth().padding(top=10.dp, start = 20.dp,end=20.dp)
                    , verticalAlignment = Alignment.CenterVertically) {
                    Text("SELECCIONA CLASE DE HERRAMIENTA", modifier = Modifier.fillMaxWidth()
                        .weight(1f), color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick ={viewModel.showMenu()} ) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_menu_24), contentDescription = "",
                            tint = Color.Black, modifier = Modifier.padding(end = 10.dp) )
                        MenuTool(uiState.showMenu, hideExpand = {viewModel.hideDialogs()}) {
                            viewModel.kindOfToolSelected(it)
                        }
                    }

                }

                DefaultTextField(value = uiState.description,
                    onValueChange = {viewModel.onChangedValues(it,uiState.pricePerDay)}, label = "DESCRIPCION",
                    modifier = Modifier.fillMaxWidth().padding(start =20.dp, end = 20.dp, bottom = 20.dp ))
                DefaultTextField(value = uiState.pricePerDay.toString(),
                    onValueChange = {viewModel.onChangedValues(uiState.description,it)}, label = "COSTE DIARIO",
                    modifier = Modifier.fillMaxWidth().padding(start =20.dp, end = 20.dp, bottom = 20.dp ),
                    keyboardType = KeyboardType.Number)
                Button(onClick = {viewModel.addNewTool()}, modifier = Modifier.fillMaxWidth().height(60.dp)
                    .padding(start =20.dp, end = 20.dp, bottom = 10.dp), shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFF0B6FBE))
                    ) {
                        Text("AÑADIR HERRAMIENTA")
                }
            }
        }

        }
    }
}

@Composable
fun DialogIdTool(show:Boolean,hideDialog:()->Unit,
                 ){
    if(show){
        Dialog(onDismissRequest ={hideDialog()} ) {
            Card(modifier = Modifier.width(300.dp).height(130.dp),
                shape = RoundedCornerShape( 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF8CE0DB))) {
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Text(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        text = "DEBE SELECCIONAR TIPO DE HERRAMIENTA", textAlign = TextAlign.Center,
                        color = Color.Red,fontWeight = FontWeight.Bold)
                    Text(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        text = "DEBE RELLENAR TODOS LOS CAMPOS", textAlign = TextAlign.Center,
                        color = Color.Red,fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(text = "FORMATO PRECIO:",color = Color.Black, textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold)
                        Text(text = "00.00", color = Color.Red, textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold)
                    }

                }
            }
        }
    }

}
@Composable
fun DialogExitNewTool(show:Boolean,onDismissRequest:()->Unit){

    if(show){
        Dialog(onDismissRequest = {onDismissRequest()}) {
            Card (modifier = Modifier.fillMaxWidth().height(180.dp).padding(20.dp),shape = RoundedCornerShape( 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)){
                Column (modifier = Modifier.fillMaxSize().padding(20.dp,40.dp), verticalArrangement = Arrangement.Center){
                    Text(modifier = Modifier.fillMaxWidth(), text = "SE HA AÑADIDO LA HERRAMIENTA",
                        textAlign = TextAlign.Center, color = Color.Black, fontWeight = FontWeight.Bold,
                        fontSize = 14.sp)
                }

            }
        }
    }
}
@Composable
fun MenuTool(expand:Boolean,hideExpand:()->Unit,
             kindOfToolSelected:(String)->Unit){

        val toolList = listOf("TALADRO","RADIAL","GRUPO SOLDAR","CORTADORA","ATORNILLADOR")
        DropdownMenu(expanded = expand, onDismissRequest = {hideExpand()},
            modifier =Modifier.background(Color.White)) {
            toolList.forEach { option->
                DropdownMenuItem(
                    text={ Text(text = option)},
                    onClick = {kindOfToolSelected(option)
                    hideExpand()}
                )

            }
        }
}