package com.victor.isotronalmacen.screeens.products

import DefaultDialogAlert
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.victor.isasturalmacen.viewModels.products.ContactorsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactorScreen(viewModel: ContactorsViewModel = hiltViewModel(),
                    navigateToHome:()->Unit,
                    navigateToBack:()->Unit,
                    navigateToManageAccount:()->Unit,
                    navigateToNewContactor:()->Unit,
                    navigateToLogin:()->Unit){

    val uiState by viewModel.uiState.collectAsState()

    Scaffold (topBar = {
        DefaultsTopAppBar(title = uiState.user,
            enableButtons = uiState.userCredentials,
            navigateToAddItem ={navigateToNewContactor()},
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
                    items(uiState.listOfContactor){
                        ProductItem(item = it, deletePermission = uiState.userCredentials,
                            showDeleteDialog = {select->viewModel.onClickDeleteContactor(select)}) {
                                select,inOut->viewModel.onClickInputOutputContactor(select,inOut)
                        }
                    }
                }
            }
            DialogInputOutputProduct(show = uiState.showDialogAmount,
                kindOfProduct = uiState.contactor.kind,
                refProduct = uiState.contactor.refProduct,
                inOutAmount = uiState.inOutAmount,
                onValueChanged = {viewModel.onValueChanged(it) },
                getInputOutputAmount = {viewModel.getInputOutAmountContactors()}) {
                viewModel.hideDialogAmount()
            }
            DefaultDialogAlert(show = uiState.showDialogError,
                dialogTitle = "ERROR", dialogText =uiState.messageError,
                onConfirmation = {viewModel.hideDialogError()},
                onDismissRequest = {viewModel.getInputOutAmountContactors()})
            DialogDeleteProduct(show = uiState.showDialogDelete,
                refProduct = uiState.contactor.refProduct,
                confirmDelete = {viewModel.deleteContactor()},
                ) { viewModel.hideDialogDelete()}

           DialogDownloadFiles(uiState.showDialogDownloadFiles,
               downloadAllProducts ={viewModel.downloadAllContactors()},
               downloadInputOutputProduct = {viewModel.downloadInputOutputContactors()},
               closeDialogDownloadFiles = {viewModel.hideDialogDownloadFiles()},
               kindOfProduct = "CONTACTOR")
            }
        }
}





