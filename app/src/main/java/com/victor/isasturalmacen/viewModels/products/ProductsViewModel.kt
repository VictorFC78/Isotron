package com.victor.isasturalmacen.viewModels.products


import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.MainActivity
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isotronalmacen.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val authService: AuthService):ViewModel() {


    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState : StateFlow<ProductsUiState> = _uiState
    init {
        _uiState.update {
            it.copy(userName = ActualUser.getActualUser().name!!)
        }
    }
   fun logout(context: Context,navigateToLogin:()->Unit){
       if(Connectivity.connectOk()==true){
           if(authService.logout()){
               ActualUser.resetActualUser()
               navigateToLogin()
           }
       }else{
           _uiState.update {
               it.copy(connectivityOk = true)
           }

       }
   }

    fun hideDialog() {
        _uiState.update { 
            it.copy(connectivityOk = false)
        }
    }

    fun closeApp(activity: Any) {

    }

}


data class ProductsUiState(val userName:String="",
                            val connectivityOk: Boolean=false,
                            val activity:MainActivity?=null)









