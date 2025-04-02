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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DeleteUserViewModel @Inject constructor(private val authService: AuthService,private val db: DataBaseService):ViewModel(){

    private var user: User = ActualUser.getActualUser()
    private val _uiState = MutableStateFlow(DeleteUserUiState())
    val uiState :StateFlow<DeleteUserUiState> = _uiState
    init {
        _uiState.update {
            it.copy(username = user.name!!, credentials =user.credentials!! )
        }
    }
    fun onValuesChanged(emailActualUser: String){
        _uiState.update {
            it.copy( emailActualUser = emailActualUser)
        }
        formIsValid()
    }
    private fun formIsValid() {

        if (Patterns.EMAIL_ADDRESS.matcher(_uiState.value.emailActualUser).matches()) {
            _uiState.update { currentState ->
                currentState.copy(enableButton = true)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(enableButton = false)
            }
        }

    }
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteActualUser(navigateToLoginScreen:()->Unit){
        var exit :Boolean = false
        if (Connectivity.connectOk()==true
            && _uiState.value.emailActualUser.equals(authService.getCurrentUser()!!.email,ignoreCase = true)){
            _uiState.update {
                it.copy(showCircularBar = true)
            }
            viewModelScope.launch {
                val deferred = withContext(Dispatchers.IO){
                    async {

                        db.deleteUserByCollection(authService.getCurrentUser()!!.email.toString())
                    }
                }
                 exit = deferred.await()
            }
            _uiState.update {
                it.copy(showCircularBar = false)
            }
              if(authService.deleteUserByEmail()){
                  hideDialogAlert()
                  navigateToLoginScreen()
              }else{
                  showDialogAlert()
              }
        }else{
            showDialogAlert()
        }
    }
    private fun showDialogAlert(){
        _uiState.update {
            it.copy(showDialogAlert = true)
        }
    }
    fun hideDialogAlert(){
        _uiState.update {
            it.copy(showDialogAlert = false, showCircularBar = false)
        }
    }
}

data class DeleteUserUiState(val username:String="",
                             val credentials:String="",
                             val emailActualUser:String="",
                             val enableButton:Boolean = false,
                             val showCircularBar:Boolean = false,
                             val showDialogAlert:Boolean=false)