package com.victor.isasturalmacen.viewModels.authentication

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.DataBaseService
import com.victor.isasturalmacen.domain.User
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
class AddUserViewModel @Inject constructor(private val authService: AuthService
,private val dataBaseService: DataBaseService
):ViewModel(){

    private val _uiState = MutableStateFlow(NewUserUiState())
    val uiState : StateFlow<NewUserUiState> = _uiState
    private val USER =  "usuario"
    init {
        val user = ActualUser.getActualUser()
        _uiState.update { state->
            state.copy(nameUser = user.name.toString())
        }
    }

    //mostar cmabios de los Texfield
    fun onAllValueChanged(email:String, password:String, name:String, job:String,repitPassword:String){
        _uiState.update { state->
            state.copy(email = email, password = password, name = name, job = job, repeatPassword = repitPassword)
        }
        formIsValid()
    }

    private fun formIsValid() {

        if (Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()
            && _uiState.value.password.length == 11
            && _uiState.value.repeatPassword==_uiState.value.password
            && _uiState.value.name.isNotBlank()
            && _uiState.value.job.isNotBlank()
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

    fun createUserWithEmailAndPassword(navigateToHome:()->Unit){
            if(Connectivity.connectOk()==true){
                viewModelScope.launch{
                    try {
                        val result =withContext(Dispatchers.IO){
                            authService.createUserWithEmailAndPassword(_uiState.value.email, _uiState.value.password)
                        }
                        if(result!=null){
                            val user = User(name = _uiState.value.name, job = _uiState.value.job, credentials = USER)
                            val dataUserOk =  withContext(Dispatchers.IO){
                                async { dataBaseService.updateFieldsUserWithEmail(_uiState.value.email,user) }
                            }
                            if(dataUserOk.isCompleted){
                                navigateToHome()
                            }else{
                                showMessage("ERROR AL CREAR USUARIO")
                            }
                        }
                    }catch (e:Exception){
                        showMessage("EL USUARIO YA EXISTE")
                    }
                }
            }else{
                _uiState.update {
                    it.copy(showDialogConnectivity = true)
                }
            }

        }


    private fun updateMessageToast(message:String){
        _uiState.update { state->
            state.copy(messageToast = message, showToast = true)

        }
    }

    fun hideDialogAndToast(){
        _uiState.update { state->
            state.copy(showToast = false, showDialogConnectivity = false)

        }
    }
    private fun cleanTextField(){
        _uiState.update { state->
            state.copy(email = "", password = "", name = "", job = "")
        }
    }
    private fun showMessage(message: String){
        updateMessageToast(message)
        cleanTextField()
    }

}


data class NewUserUiState(
    val email:String="",
    val password:String="",
    val name:String="",
    val job:String="",
    val repeatPassword:String ="",
    val enable:Boolean=false,
    val nameUser:String="",
    val showToast:Boolean=false,
    val messageToast:String="",
    val showDialogConnectivity:Boolean=false)