package com.victor.isasturalmacen.screens.products

import DefaultDialogAlert
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.viewModels.products.NewWireViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp
import com.victor.isotronalmacen.components.DefaultTextField
import com.victor.isotronalmacen.components.DefaultsTopAppBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWireScreen(viewModel: NewWireViewModel = hiltViewModel(),
                  navigateToBack:()->Unit,
                  navigateToHome:()->Unit,
                  navigateToManageAccount:()->Unit
                  , navigateToWires:()->Unit,
                  navigateToLogin:()->Unit) {

    val uiState by viewModel.uiState.collectAsState()
    Scaffold(topBar = {
        DefaultsTopAppBar(enableButtons = false, title = uiState.user)
    },
        bottomBar = {
            DefaultBottomBarApp(navigateToBack = { navigateToBack() },
                navigateToHome = { navigateToHome() },
                navigateToManageAccount = { navigateToManageAccount() },
                enableDeleteUser = false,
                enableChangePassword = false
            ) {
                viewModel.logOut { navigateToLogin() }
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.Black),
            contentAlignment = Alignment.TopStart
        ) {
            Image(painter = painterResource(R.drawable.lineas), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(), alpha = 0.75f)
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.logo_isotron), contentDescription = "",
                    modifier = Modifier.padding(20.dp)
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Card(
                        modifier = Modifier.fillMaxWidth().height(480.dp).padding().alpha(0.75f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                            DefaultTextField(
                                value = uiState.kind, onValueChange = {
                                    viewModel.onValuesChanged(
                                        it,
                                        uiState.manufacturer,
                                        uiState.refProduct,
                                        uiState.pricePerUnit,
                                        uiState.stockActual
                                    )
                                },
                                label = "TIPO DE CABLE",
                                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                            )
                            DefaultTextField(
                                value = uiState.manufacturer, onValueChange = {
                                    viewModel.onValuesChanged(
                                        uiState.kind,
                                        it,
                                        uiState.refProduct,
                                        uiState.pricePerUnit,
                                        uiState.stockActual
                                    )
                                }, label = "FABRICANTE",
                                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                            )
                            DefaultTextField(
                                value = uiState.refProduct, onValueChange = {
                                    viewModel.onValuesChanged(
                                        uiState.kind,
                                        uiState.manufacturer,
                                        it,
                                        uiState.pricePerUnit,
                                        uiState.stockActual
                                    )
                                }, label = "REF.PRODUCTO",
                                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                            )
                            DefaultTextField(
                                value = uiState.pricePerUnit, onValueChange = {
                                    viewModel.onValuesChanged(
                                        uiState.kind,
                                        uiState.manufacturer,
                                        uiState.refProduct,
                                        it,
                                        uiState.stockActual
                                    )
                                }, label = "PRECIO UNITARIO",
                                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                                keyboardType = KeyboardType.Number
                            )
                            DefaultTextField(
                                value = uiState.stockActual, onValueChange = {
                                    viewModel.onValuesChanged(
                                        uiState.kind,
                                        uiState.manufacturer,
                                        uiState.refProduct,
                                        uiState.pricePerUnit,
                                        it
                                    )
                                }, label = "UNIDADES",
                                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                                keyboardType = KeyboardType.Number
                            )
                            Button(
                                modifier = Modifier.fillMaxWidth().height(60.dp),
                                onClick = { viewModel.addNewWire { navigateToWires() } },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF2196F3
                                    )
                                ),
                                shape = RectangleShape,
                                enabled = uiState.enableButton
                            ) {
                                Text(
                                    text = "AÑADIR",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp
                                )
                            }
                        }

                    }
                }
                DefaultDialogAlert(show = uiState.showDialogError,
                    onConfirmation = { viewModel.hideDialog() },
                    onDismissRequest = { viewModel.hideDialog() },
                    dialogTitle = "ERROR",
                    dialogText = uiState.messageDialog
                )
            }
        }
    }
}