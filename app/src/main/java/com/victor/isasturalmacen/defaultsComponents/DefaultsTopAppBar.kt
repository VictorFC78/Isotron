package com.victor.isotronalmacen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.victor.isasturalmacen.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultsTopAppBar(
    backgroundColor:Color=Color.LightGray,
    enableButtons:Boolean = true,
    enableUpFile:Boolean = false,
    enableSelectFile:Boolean=false,
    title:String = "",
    onClickSelectFile:()->Unit={},
    onClickUpFile:()->Unit={},
    navigateToAddItem:()->Unit ={},
    onClickDownloadItem:()->Unit = {}
) {

    TopAppBar(title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor),
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text=title, modifier = Modifier.padding(10.dp), fontSize = 22.sp)
                    Spacer(modifier = Modifier.weight(1f))
                if(enableUpFile){
                    IconButton(onClick = { onClickUpFile() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_file_upload_24),
                            contentDescription = ""
                        )
                    }
                }
                if (enableButtons) {
                    IconButton(onClick = { navigateToAddItem() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                            contentDescription = ""
                        )
                    }
                    IconButton(onClick = {onClickDownloadItem()  }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_file_download_24),
                            contentDescription = ""
                        )
                    }
                }
            }
        })

}
@Composable
@Preview
fun Prueba(){
    DefaultsTopAppBar()
}