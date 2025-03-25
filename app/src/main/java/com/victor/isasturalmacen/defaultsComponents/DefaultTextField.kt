package com.victor.isotronalmacen.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun DefaultTextField(
    modifier: Modifier=Modifier,
    value: String,
    onValueChange:(String)->Unit,
    label: String ="",
    icon: ImageVector?=null,
    keyboardType: KeyboardType=KeyboardType.Text,
    visualTransformation:VisualTransformation = VisualTransformation.None){

    TextField(value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {Text(label)},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = {
            if(icon!=null){
                Icon(icon, contentDescription = "")
            }
        },
        singleLine = true, maxLines = 1,
        visualTransformation = visualTransformation

    )
}