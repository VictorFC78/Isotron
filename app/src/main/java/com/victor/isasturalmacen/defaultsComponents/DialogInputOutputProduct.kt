package com.victor.isotronalmacen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DialogInputOutputProduct(show:Boolean,
                             kindOfProduct:String,
                             refProduct:String,
                             inOutAmount:String,
                             onValueChanged:(String)->Unit,
                             getInputOutputAmount:()->Unit,
                             hideDialogInsert:()->Unit){
    if(show){
        Column (modifier = Modifier.fillMaxSize()){
            Spacer(modifier = Modifier.weight(1f))
            Card (modifier = Modifier.fillMaxWidth().height(200.dp).alpha(0.75f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart =10.dp, topEnd = 10.dp )
            ){
                Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                    Text(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp) ,
                        text = "PRODUCTO:${kindOfProduct}  REF:${refProduct}", fontWeight = FontWeight.Bold)
                    TextField(value =inOutAmount, onValueChange = {onValueChanged(it)}, label = { Text("CANTIDAD") }
                        , singleLine = true, maxLines = 1, modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceBetween){
                        Button(onClick = {getInputOutputAmount()}, modifier = Modifier.width(180.dp).padding(top = 10.dp),
                            shape = RectangleShape, colors = ButtonDefaults.buttonColors(containerColor = Color(
                                0xFF2196F3)
                            )) {
                            Text("ACEPTAR", color = Color.White)
                        }
                        Button(onClick = {hideDialogInsert()}, modifier = Modifier.width(180.dp).padding(top = 10.dp),
                            shape = RectangleShape, colors = ButtonDefaults.buttonColors(containerColor = Color(
                                0xFF2196F3)
                            )) {
                            Text("CANCELAR", color = Color.White)
                        }
                    }

                }
            }
        }
    }

}