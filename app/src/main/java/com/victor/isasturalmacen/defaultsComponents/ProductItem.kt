package com.victor.isotronalmacen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.domain.Product

import org.checkerframework.checker.units.qual.A

@Composable
fun ProductItem(deletePermission:Boolean,item: Product,
                showDeleteDialog:(Product)->Unit,
                onClickShowInputOutputProduct: (Product, Boolean) -> Unit){

    Box(modifier = Modifier.fillMaxSize().alpha(0.75f), contentAlignment = Alignment.Center){
        Row(modifier = Modifier.fillMaxWidth().height(160.dp).padding(bottom = 10.dp)
            .background(Color.LightGray)){
            Row {
                Column {
                    Text(text = "ID: ${item.id}", modifier = Modifier
                        .padding( top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
                        color = Color.Black, fontWeight = FontWeight.Bold)
                    Text(text = "TIPO: ${item.kind}", modifier = Modifier
                        .padding( bottom = 10.dp, start = 20.dp, end = 20.dp),
                        color = Color.Black,fontWeight = FontWeight.Bold)
                    Text(text = "STOCK ACTUAL: ${item.stockActual}", modifier = Modifier
                        .padding( bottom = 10.dp, start = 20.dp, end = 20.dp),
                        color = Color.Black,fontWeight = FontWeight.Bold)
                    Text(text = "REF.FABRICANTE: ${item.refProduct}", color = Color.Black, modifier =
                    Modifier.padding( start = 20.dp, end = 20.dp),fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically){
                        Text("RECEPCIONAR", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {onClickShowInputOutputProduct(item,true)}) {
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_login_24), contentDescription = "",
                                tint = Color.Black)
                        }
                        if(item.stockActual>0){
                            Text("DESPACHAR", fontWeight = FontWeight.Bold)
                            IconButton(onClick = {onClickShowInputOutputProduct(item,false)}) {
                                Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24), contentDescription = "",
                                    tint = Color.Black)
                            }
                        }
                        if(deletePermission){
                            IconButton(onClick = {showDeleteDialog(item)}) {
                                Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_24), contentDescription = "",
                                    tint = Color.White)
                            }
                        }

                    }
                }

            }

        }
    }
}