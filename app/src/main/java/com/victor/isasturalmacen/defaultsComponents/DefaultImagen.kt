package com.victor.isotronalmacen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DefaultImagen(
                  @DrawableRes imagen:Int,
                  descripcion:String="",
                  clikable:()->Unit){
    Image(painter = painterResource(imagen),
        contentDescription = descripcion,
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)
            .clip(CircleShape)
            .clickable { clikable() },
        alignment = Alignment.Center
        )

}