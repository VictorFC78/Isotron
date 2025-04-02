package com.victor.isotronalmacen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun DialogDeleteProduct(show:Boolean,
                        refProduct:String,
                        confirmDelete:()->Unit,
                        cancelDelete:()->Unit){
    if(show){
        Card(modifier = Modifier.width(320.dp).height(200.dp).alpha(0.85f),
            colors = CardDefaults.cardColors(containerColor = Color.White)){
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                Text(text = "¿QUIERE BORRAR?:${refProduct}", modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 20.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                OutlinedButton(onClick = {confirmDelete()}, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    Text("ACEPTAR")
                }
                OutlinedButton(onClick = {cancelDelete()}, modifier = Modifier.fillMaxWidth()) {
                    Text("CANCELAR")
                }
            }
        }
    }

}