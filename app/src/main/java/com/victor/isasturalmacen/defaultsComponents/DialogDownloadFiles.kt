package com.victor.isotronalmacen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.victor.isasturalmacen.R


@Composable
fun DialogDownloadFiles(show:Boolean,
                        downloadAllProducts:()->Unit,
                        downloadInputOutputProduct:()->Unit,
                        closeDialogDownloadFiles:()->Unit,
                        kindOfProduct: String=""){

    if(show){
        Card (modifier = Modifier.width(380.dp).height(180.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)){
            Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(text = "DESCARGA DE ARCHIVOS", modifier = Modifier.weight(1f).padding(start = 30.dp), fontWeight = FontWeight.Bold)
                    IconButton(onClick ={closeDialogDownloadFiles()} ) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_close_24), contentDescription = "")
                    }
                }
                OutlinedButton(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), onClick = {downloadAllProducts()}) {
                    Text("LISTA DE ${kindOfProduct.uppercase()}")
                }
                OutlinedButton(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), onClick = {downloadInputOutputProduct()}) {
                    Text("ENTRADAS / SALIDAS DE ${kindOfProduct.uppercase()}")
                }
            }
        }
    }

}