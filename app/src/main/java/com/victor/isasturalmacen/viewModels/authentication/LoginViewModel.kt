package com.victor.isasturalmacen.viewModels.authentication

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.data.DataBaseService
import com.victor.isotronalmacen.data.AuthService

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService, private val db: DataBaseService):ViewModel() {


  private val  _uiState = MutableStateFlow(LoginScreenState())
    val uiState :StateFlow<LoginScreenState> = _uiState

    fun onChangedEmailAndPasswordValues(email:String, password:String){
        _uiState.update {
            currentState-> currentState.copy(email =email,password=password )
        }
        formIsValid()
    }


    private fun formIsValid() {

        if (Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()
            && _uiState.value.password.length == 11
        ) {
            _uiState.update { currentState ->
                currentState.copy(enable = true)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(enable = false)
            }
        }

    }
    fun login(email:String, password:String,navigateToHome:()->Unit){
        if(Connectivity.connectOk() == true){

            viewModelScope.launch {
                _uiState.update { it.copy(showCircularProgressBar = true) }
                try {
                    val result:FirebaseUser? = withContext(Dispatchers.IO){
                        authService.login(email, password)
                    }
                    hideCircularProgressBar()
                    if (result !=null){
                        navigateToHome()
                        db.recoverDataActualUser(_uiState.value.email)
                    }else{
                        _uiState.update { state->
                            state.copy(show=true)
                        }
                    }
                }catch (e:Exception){
                    hideCircularProgressBar()
                    _uiState.update { state->
                        state.copy(show=true)
                    }
                }

            }
        }else{
            _uiState.update { state->
                state.copy(showConnectivityAlert = true)
            }
        }

    }
    fun checkDestinationLogin(): Destination {
          return  if (isUserLogged()){
              Destination.Home
            }else{
              Destination.Login
            }
    }
    private fun isUserLogged():Boolean{
      return  authService.isUserLogged()
    }

    fun hideAlertDialog(){
        _uiState.update { state->
            state.copy(show = false, showConnectivityAlert = false)

        }
    }
    private fun hideCircularProgressBar(){
        _uiState.update { it.copy(showCircularProgressBar = false) }
    }

}
data class LoginScreenState(
    val email:String = "",
    val password:String = "",
    val enable:Boolean = false,
    val logged:Boolean = false,
    val show:Boolean = false,
    val showConnectivityAlert:Boolean=false,
    val showCircularProgressBar:Boolean=false)

sealed class  Destination{
    object Login: Destination()
    object Home: Destination()
}