package com.victor.isasturalmacen.screens.tools

import DefaultDialogAlert
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.domain.Tool
import com.victor.isasturalmacen.viewModels.tools.ToolFlowViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import com.victor.isotronalmacen.components.DefaultsTopAppBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(viewModel: ToolFlowViewModel = hiltViewModel(),
                   navigateToBack:()->Unit,
                   navigateToHome:()->Unit,
                   navigateToManageAccount:()->Unit,
                   navigateToAddTool:()->Unit,
                   navigateToLogout:()->Unit){


    val uiState by viewModel.uiState.collectAsState()
    val actualUser = uiState.userCredentianls.equals("admin")

    Scaffold(topBar = {
        DefaultsTopAppBar(enableButtons = actualUser, navigateToAddItem ={ navigateToAddTool()},
            onClickDownloadItem = {viewModel.showDownLoadInfoDialog()}, title = uiState.userName)}, bottomBar = {
            DefaultBottomBarApp(navigateToBack={navigateToBack() },
                enableChangePassword = false,
                navigateToHome = {navigateToHome()},
                navigateToManageAccount = {navigateToManageAccount()}) {
                viewModel.logout() { navigateToLogout() }
            }
        }) { paddingValues ->
        Box(modifier= Modifier.fillMaxSize().padding(paddingValues)
            .background(Color.Black)){
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                LazyColumn (modifier = Modifier.fillMaxWidth()){
                    items(uiState.listOfTools){
                      ItemTool(tool = it, takeoutAndReturnTool = { tool,inOut->viewModel.takeOutAndReturnTool(tool,inOut) },
                          deleteTool = { dt->viewModel.deleteTool(dt)})

                    }
                }
            }
            DefaultDialogAlert(show = uiState.connectivityOk, dialogText = "Si Acepta Cerrará la Aplicacion manteniendo la sesion",
                dialogTitle = "SIN CONEXION", onDismissRequest = {viewModel.hideDialog()}, onConfirmation = {viewModel.closeApp()})
            DialogInfoDownLoad(uiState.showDownLoadInfoDialog, downLoadAllTools = {viewModel.downloadAllTools()},
                downLoadInputs = {viewModel.downloadAllRegisterInputsOutputTools()},
                downLoadOutputs = {viewModel.downloadAllRegisterDeleteTolls()}, hideDialog = {viewModel.hideDialog()})
        }
    }

}
@Composable
fun DialogInfoDownLoad(show:Boolean,
                       downLoadAllTools:()->Unit,
                       downLoadOutputs:()->Unit,
                       downLoadInputs:()->Unit,
                       hideDialog:()->Unit) {
    if (show) {
        Dialog(onDismissRequest = { hideDialog() }) {
            Card(
                modifier = Modifier.size(width = 360.dp, height = 220.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    text = "DESGARGAR ARCHIVOS",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    OutlinedButton(
                        onClick = { downLoadAllTools() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(
                                0xFF2196F3
                            )
                        )
                    ) {
                        Text("LISTADO HERRAMIENTAS", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { downLoadInputs() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(
                                0xFF2196F3
                            )
                        )
                    ) {
                        Text("LISTADO ENTRADAS HERRAMIENTAS", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { downLoadOutputs() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(
                                0xFF2196F3
                            )
                        )
                    ) {
                        Text("LISTADO HERRAMIENTAS ELIMINADAS", color = Color.White)
                    }
                }


            }
        }
    }
}

    @Composable
    fun ItemTool(takeoutAndReturnTool:(Tool,Boolean)->Unit,
                 tool: Tool,deleteTool:(Tool)->Unit) {

        val picture = when {
            tool.id!!.contains(other = "taladro", ignoreCase = true) -> R.drawable.taladro
            tool.id.contains(other = "amoladora", ignoreCase = true) -> R.drawable.amoladora
            tool.id.contains(other = "grupo", ignoreCase = true) -> R.drawable.gsoldar
            tool.id.contains(other = "CORTADORA", ignoreCase = true) -> R.drawable.cortadora
            tool.id.contains(other = "Atornillador", ignoreCase = true) -> R.drawable.atronillador
            else -> {
                R.drawable.sierra
            }
        }
        val inStore = if (tool.inStore == true) "SI" else "NO"
        Row(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            Image(
                painter = painterResource(picture),
                contentDescription = "",
                modifier = Modifier.padding(5.dp)
            )
            Column() {
                Text(text = "ID:${tool.id}")
                Text(text = "TIPO:${tool.description}")
                Row() {
                    Spacer(modifier = Modifier.weight(1f))
                    if (tool.inStore == true) {
                        Text(text = "SACAR")
                        IconButton(onClick = { takeoutAndReturnTool(tool,false) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24),
                                contentDescription = "", tint = Color(0xFF2196F3)
                            )
                        }
                    } else {
                        Text(text = "DEVOLVER")
                        IconButton(onClick = { takeoutAndReturnTool(tool,true) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24),
                                contentDescription = "", tint = Color(0xFF2196F3)
                            )
                        }
                    }
                    IconButton(onClick = { deleteTool(tool) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_24),
                            contentDescription = "", tint = Color(0xFF2196F3)
                        )

                    }
                }
            }
        }

    }

