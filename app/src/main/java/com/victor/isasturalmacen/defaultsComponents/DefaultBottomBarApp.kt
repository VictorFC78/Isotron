package com.victor.isotronalmacen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.victor.isasturalmacen.R


@ExperimentalMaterial3Api
@Composable
fun DefaultBottomBarApp(navigateToBack:()->Unit,
                        navigateToHome:()->Unit ={}, enableToHome:Boolean=true,
                        navigateToManageAccount:()->Unit={}, enableManageAccount:Boolean=true,
                        onClickChangePassword:()->Unit={}, enableChangePassword:Boolean=true,
                        navigateToDeleteUser:()->Unit={}, enableDeleteUser:Boolean=false,
                        navigateToLogin:()->Unit) {
    BottomAppBar(containerColor = Color.LightGray,
        actions ={
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                IconButton(onClick = {navigateToBack()}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
                if(enableToHome){
                    IconButton(onClick = {navigateToHome()}) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_home_24), contentDescription = "")
                    }
                }
                if(enableManageAccount){
                    IconButton(onClick = {navigateToManageAccount()}) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_manage_accounts_24), contentDescription = "")
                    }
                }
                if(enableDeleteUser){
                    IconButton(onClick = {navigateToDeleteUser()}) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_person_remove_24), contentDescription = "")
                    }
                }
                if(enableChangePassword){
                    IconButton(onClick = {onClickChangePassword()}) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_lock_reset_24), contentDescription = "")
                    }
                }
                IconButton(onClick = {navigateToLogin()}) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24), contentDescription = "")
                }
            }
        } )
}