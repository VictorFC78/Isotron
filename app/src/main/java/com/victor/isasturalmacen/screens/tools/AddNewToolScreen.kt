package com.victor.isasturalmacen.screens.tools

import DefaultDialogAlert
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.victor.isasturalmacen.viewModels.tools.AddToolUiState
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
            contentAlignment = Alignment.TopCenter
        ){
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Image(painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
                modifier = Modifier.fillMaxWidth().padding(20.dp))
           DialogIdTool (show = uiState.showHelpDialog){
               viewModel.hideDialogs()
           }

            DefaultDialogAlert(show = uiState.showErrorDialog, onConfirmation = {viewModel.hideDialogs()},
                onDismissRequest = {viewModel.hideDialogs()}, dialogTitle = "ERROR", dialogText = "se ha producido un error al crear la herramienta")
            DefaultDialogAlert(show = uiState.showConnectivityOk, dialogTitle = "SIN CONEXION", dialogText = "Sin conexion ,accion cancelada",
                onConfirmation = {viewModel.hideDialogs()}, onDismissRequest = {viewModel.hideDialogs()})

            MenuAddNewKingOfTool(uiState = uiState, addNewKindOfTool = {viewModel.addNewKindOfTool()},
                onValuedChanged = {viewModel.onChangedValueNewKinOfTool(it)}) {
                viewModel.hideDialogs()
            }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))
            Card (modifier = Modifier.fillMaxWidth().height(320.dp).alpha(0.75f),
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
                        MenuTool(list = uiState.listOfKidOfTools,expand = uiState.showMenu, hideExpand = {viewModel.hideDialogs()}) {
                            viewModel.kindOfToolSelected(it)
                        }
                    }
                    IconButton(onClick ={viewModel.showMenuNewKindOfTool()} ) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_add_box_24), contentDescription = "",
                            tint = Color.Black, modifier = Modifier.padding(end = 10.dp) )

                    }

                }
                Text(text ="TIPO DE HERRAMIENTA:${uiState.kindOfToolSelected}" , modifier = Modifier.
                fillMaxWidth().
                padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
                    fontWeight = FontWeight.Black)
                DefaultTextField(value = uiState.description,
                    onValueChange = {viewModel.onChangedValues(it,uiState.pricePerDay)}, label = "DESCRIPCION",
                    modifier = Modifier.fillMaxWidth().padding(start =20.dp, end = 20.dp, bottom = 20.dp ))
                DefaultTextField(value = uiState.pricePerDay.toString(),
                    onValueChange = {viewModel.onChangedValues(uiState.description,it)}, label = "COSTE DIARIO",
                    modifier = Modifier.fillMaxWidth().padding(start =20.dp, end = 20.dp, bottom = 20.dp ),
                    keyboardType = KeyboardType.Number)
                Button(onClick = {viewModel.addNewTool()}, modifier = Modifier.fillMaxWidth().height(60.dp)
                    .padding(start =20.dp, end = 20.dp, bottom = 10.dp), shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFF0B6FBE)),
                    enabled = uiState.enableButtonAdd
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
            Card(modifier = Modifier.width(300.dp).height(60.dp),
                shape = RoundedCornerShape( 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(text = "FORMATO PRECIO:",color = Color.Black, textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold)
                        Text(text = "45.98  -  125", color = Color.Red, textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold)
                    }

                }
            }
        }
    }

}

@Composable
fun MenuTool(list:List<String>,expand:Boolean,hideExpand:()->Unit,
             kindOfToolSelected:(String)->Unit){

        DropdownMenu(expanded = expand, onDismissRequest = {hideExpand()},
            modifier =Modifier.background(Color.White)) {
            list.forEach { option->
                DropdownMenuItem(
                    text={ Text(text = option)},
                    onClick = {kindOfToolSelected(option)
                    hideExpand()}
                )

            }
        }
}
@Composable
fun MenuAddNewKingOfTool(uiState:AddToolUiState,
                         addNewKindOfTool:()->Unit,
                         onValuedChanged:(String)->Unit,
                         hideDialog:()->Unit){
    if(uiState.showMenuNewKindOfTool){
        Column(modifier = Modifier.fillMaxSize().padding(vertical = 100.dp, horizontal = 20.dp)) {
            Card(modifier =Modifier.size(width = 400.dp, height = 250.dp).padding(),
                colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                    Text(text = "INSERTE TIPO DE HERRAMIENTA", modifier = Modifier.fillMaxWidth().padding(top=10.dp,bottom = 10.dp))
                    DefaultTextField(value = uiState.newKindOfTool, onValueChange = {onValuedChanged(it)},
                        modifier =Modifier.fillMaxWidth().padding(bottom = 10.dp) ,
                        label = "TIPO DE HERRAMIENTA")
                    OutlinedButton(onClick = {addNewKindOfTool()}, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                        Text("ACEPTAR")
                    }
                    OutlinedButton(onClick = {hideDialog()}, modifier = Modifier.fillMaxWidth()) {
                        Text("CANCELAR")
                    }
                }
            }
        }

    }

}