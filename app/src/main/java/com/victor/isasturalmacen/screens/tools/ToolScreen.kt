package com.victor.isasturalmacen.screens.tools

import DefaultDialogAlert
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.defaultsComponents.ToastDefault
import com.victor.isasturalmacen.domain.Tool
import com.victor.isasturalmacen.viewModels.tools.ToolFlowUiState
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
            .background(Color.Black), contentAlignment = Alignment.Center){

            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                LazyColumn (modifier = Modifier.fillMaxWidth()){
                    items(uiState.listOfTools){ tool ->
                        ItemTool(tool = tool, deletePermission = actualUser,
                            takeoutAndReturnTool = { tools,inOut->viewModel.takeOutAndReturnTool(tools,inOut) },
                          deleteTool = { viewModel.showDeleteDialog(it)})

                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize().padding(10.dp), contentAlignment = Alignment.BottomStart){
                FloatingActionButton(containerColor = Color.LightGray,
                    onClick = {  }, modifier = Modifier.alpha(0.80f)
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            }

            DefaultDialogAlert(show = uiState.connectivityOk, dialogText = uiState.messageAlert,
                dialogTitle = uiState.tipoAlert, onDismissRequest = {viewModel.hideDialog()}, onConfirmation = {viewModel.hideDialog()})
            DialogInfoDownLoad(uiState.showDownLoadInfoDialog, downLoadAllTools = {viewModel.downloadAllTools()},
                downLoadDeleteTools = {viewModel.downloadAllRegisterDeleteTolls()},
                downLoadInOutputs ={viewModel.downloadAllRegisterInputsOutputTools()}, hideDialog = {viewModel.hideDialog()})
            DialogDeleteTool(toolFlow = uiState, show=uiState.showDeleteDialog,
                hideDialog = {viewModel.hideDialog() })
            {viewModel.deleteTool()}


        }
    }

}
@Composable
fun DialogInfoDownLoad(show:Boolean,
                       downLoadAllTools:()->Unit,
                       downLoadInOutputs:()->Unit,
                       downLoadDeleteTools:()->Unit,
                       hideDialog:()->Unit) {
    if (show) {
        Dialog(onDismissRequest = { }) {

            Card(
                modifier = Modifier.size(width = 460.dp, height = 230.dp).alpha(0.75f),
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
                        onClick = { downLoadAllTools() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                        )
                    ) {
                        Text("LISTADO HERRAMIENTAS", color = Color.Black)
                    }
                    OutlinedButton(
                        onClick = { downLoadInOutputs() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(

                        )
                    ) {
                        Text("ENTRADA/SALIDA HERRAMIENTAS", color = Color.Black)
                    }
                    OutlinedButton(
                        onClick = { downLoadDeleteTools() }, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(

                        )
                    ) {
                        Text("LISTADO HERRAMIENTAS ELIMINADAS", color = Color.Black)
                    }
                }

            }
        }
    }
}

    @Composable
    fun ItemTool(deletePermission:Boolean,takeoutAndReturnTool:(Tool,Boolean)->Unit,
                 tool: Tool,deleteTool:(Tool)->Unit) {

        val picture = when {
            tool.id!!.contains(other = "taladro", ignoreCase = true) -> R.drawable.taladro
            tool.id.contains(other = "Radial", ignoreCase = true) -> R.drawable.amoladora
            tool.id.contains(other = "grupo", ignoreCase = true) -> R.drawable.gsoldar
            tool.id.contains(other = "CORTADORA", ignoreCase = true) -> R.drawable.cortadora
            tool.id.contains(other = "Atornillador", ignoreCase = true) -> R.drawable.atronillador
            tool.id.contains(other = "sierra", ignoreCase = true) -> R.drawable.atronillador
            else -> {
                R.drawable.general_
            }
        }
        val inStore = if (tool.inStore == true) "SI" else "NO"
        Row(modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 10.dp)
            .background(Color.LightGray).alpha(0.75f)) {
            Image(
                painter = painterResource(picture),
                contentDescription = "",
                modifier = Modifier.padding(5.dp)
            )
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(text = "${tool.id}", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
                Text(text = "${tool.description}", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.weight(1f))
                    if (tool.inStore == true) {
                        Text(text = "SACAR", fontWeight = FontWeight.Bold)
                        IconButton(onClick = { takeoutAndReturnTool(tool,false) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24),
                                contentDescription = "", tint = Color.Black
                            )
                        }
                    } else {
                        Text(text = "DEVOLVER", fontWeight = FontWeight.Bold)
                        IconButton(onClick = { takeoutAndReturnTool(tool,true) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24),
                                contentDescription = "", tint = Color.Black
                            )
                        }
                    }
                    if(deletePermission){
                        IconButton(onClick = { deleteTool(tool) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_24),
                                contentDescription = "", tint = Color.Black
                            )

                        }
                    }

                }
            }
        }

    }
@Composable
fun DialogDeleteTool(toolFlow:ToolFlowUiState,show:Boolean,hideDialog:()->Unit,
                     deleteTool:()->Unit){
    if(show){
        Card(modifier = Modifier.width(300.dp).height(180.dp),colors=CardDefaults.cardColors(Color.White)){
            Column (modifier = Modifier.fillMaxSize().padding(10.dp)){
                Text(text = "¿Esta seguro de borrar\n la herramienta:${toolFlow.toolSelected.id}?", modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center, fontSize = 16.sp)
                OutlinedButton(onClick = {deleteTool()}, modifier =Modifier.fillMaxWidth().padding(10.dp) ) {
                    Text("ACEPTAR", color = Color.Black)
                }
                OutlinedButton(onClick = {hideDialog()}, modifier =Modifier.fillMaxWidth()
                    .padding(bottom = 10.dp, start = 10.dp,end=10.dp) ) {
                    Text("CANCELAR", color = Color.Black)
                }
            }
        }
    }

}
