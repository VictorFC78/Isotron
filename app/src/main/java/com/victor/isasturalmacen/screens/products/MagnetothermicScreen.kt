package com.victor.isotronalmacen.screeens.products

import DefaultDialogAlert
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import com.victor.isotronalmacen.components.DefaultsTopAppBar
import com.victor.isotronalmacen.components.DialogDeleteProduct
import com.victor.isotronalmacen.components.DialogDownloadFiles
import com.victor.isotronalmacen.components.DialogInputOutputProduct
import com.victor.isotronalmacen.components.ProductItem
import com.victor.isotronalmacen.viewmodels.products.MagnetothermicViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagnetothermicScreen(viewModel:MagnetothermicViewModel= hiltViewModel(),
                         navigateToBack:()->Unit,
                         navigateToHome:()->Unit,
                         navigateToManageAccount:()->Unit,
                         navigateToNewMagneto:()->Unit,
                         navigateToLogin:()->Unit){

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold (topBar = {
        DefaultsTopAppBar(title = uiState.user,
            enableButtons = uiState.userCredentials,
            navigateToAddItem ={navigateToNewMagneto()},
            onClickDownloadItem = {viewModel.showDialogDownloadFiles()})
    }, bottomBar = {
        DefaultBottomBarApp(navigateToBack = {navigateToBack()}, enableChangePassword = false, enableDeleteUser = false,
             navigateToHome = {navigateToHome()}, navigateToManageAccount = {navigateToManageAccount()}) {
                viewModel.logOut { navigateToLogin() }
        }
    }){ paddingValues ->
        Box (modifier = Modifier.fillMaxSize().padding(paddingValues).
        background(Color.Black), contentAlignment = Alignment.Center){
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Column (modifier = Modifier.fillMaxSize().padding(10.dp)){
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(uiState.listOfMagnetothermic){
                        ProductItem(item=it, deletePermission = uiState.userCredentials,
                            showDeleteDialog = {magneto->viewModel.onClickDeleteMagneto(magneto)}) {
                            select,inOut->viewModel.onClickInputOutputMagneto(select,inOut)
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
            DialogInputOutputProduct(show = uiState.showDialogAmount,
                kindOfProduct = uiState.magnetothermic.kind,
                refProduct = uiState.magnetothermic.refProduct,
                inOutAmount = uiState.inOutAmount,
                onValueChanged = {viewModel.onValueChanged(it) },
                getInputOutputAmount = {viewModel.getInputOutAmountMagnetos()}) {
                viewModel.hideDialogAmount()
            }
            DefaultDialogAlert(show = uiState.showDialogError,
                dialogTitle = "ERROR", dialogText =uiState.messageError,
                onConfirmation = {viewModel.hideDialogError()},
                onDismissRequest = {viewModel.getInputOutAmountMagnetos()})
            DialogDeleteProduct(show = uiState.showDialogDelete,
                refProduct = uiState.magnetothermic.refProduct,
                confirmDelete = {viewModel.deleteMagneto()},
            ) { viewModel.hideDialogDelete()}

            DialogDownloadFiles(uiState.showDialogDownloadFiles,
                downloadAllProducts ={viewModel.downloadAllMagnetos()},
                downloadInputOutputProduct = {viewModel.downloadInputOutputMagnetos()},
                closeDialogDownloadFiles = {viewModel.hideDialogDownloadFiles()},
                kindOfProduct = "MAGNETOTERMICO")
        }
    }
}