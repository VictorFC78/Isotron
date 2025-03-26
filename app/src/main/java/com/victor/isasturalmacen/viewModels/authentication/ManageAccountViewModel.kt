package com.victor.isasturalmacen.viewModels.authentication

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.MainActivity
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isotronalmacen.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ManageAccountViewModel @Inject constructor(private val authService: AuthService):ViewModel(){

    private val _ManageAccount_uiState = MutableStateFlow(ManageAccountUiState())
    val manageAccountUiState :StateFlow<ManageAccountUiState> =_ManageAccount_uiState

    fun onAllValuesChanged(newPassword: String,repeatNewPassword: String){
        _ManageAccount_uiState.update { state->
            state.copy(newPassword = newPassword, repeatNewPassword = repeatNewPassword)
        }
        formIsValid()
    }
    private fun formIsValid(){
        when{
                     _ManageAccount_uiState.value.newPassword.length==11
                    && _ManageAccount_uiState.value.newPassword==_ManageAccount_uiState.value.repeatNewPassword->{
                        _ManageAccount_uiState.update { state->
                            state.copy(formIsValid = true)
                        }
                    }
            else->{
                _ManageAccount_uiState.update { state->
                    state.copy(formIsValid = false)

                }
            }
        }
    }

    fun logOut(navigateToLogin:()->Unit){
        if(Connectivity.connectOk()==true){
            if(authService.logout()){
                navigateToLogin()
            }else{
                //mostramos un Dialogo
            }
        }else{
            _ManageAccount_uiState.update {
                it.copy(showConnectivityOk = true)
            }
        }

    }
    fun onClickChangePassword(){
        _ManageAccount_uiState.update { state->
           state.copy(showInputData = !_ManageAccount_uiState.value.showInputData)
        }

    }

    fun changePassword(newPassword: String){

        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                try {
                    val result = withContext(Dispatchers.IO){
                        async {authService.changePassword(newPassword)   }
                    }
                    onClickChangePassword()
                    updateMessageToast("Contraseña Cambiada")
                }catch (e:Exception){
                    updateMessageToast("Se ha producido un error")
                }
            }
        }else{
            _ManageAccount_uiState.update {
                it.copy(showConnectivityOk = true)
            }
        }

    }
    fun closeApp(){
        val activity = Connectivity.getContext() as MainActivity
        activity.finish()
    }
    private fun connectivityOk(context: Context):Boolean{
        val con = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return con.activeNetworkInfo?.isConnected?:false
    }
    private fun updateMessageToast(message:String){
        _ManageAccount_uiState.update { state->
            state.copy(messageToast = message, showToast = true)
        }
    }
    fun hideToastAndDialog(){
        _ManageAccount_uiState.update { state->
            state.copy(showToast = false, showConnectivityOk = false)
        }
    }
}

data class ManageAccountUiState(
    val newPassword:String ="",
    val repeatNewPassword:String ="",
    val showInputData:Boolean = false,
    val formIsValid:Boolean =false,
    val showToast:Boolean = false,
    val messageToast:String ="",
    val showConnectivityOk:Boolean=false
)
