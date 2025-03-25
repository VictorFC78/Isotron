package com.victor.isasturalmacen.viewModels.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.MainActivity
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.domain.User
import com.victor.isotronalmacen.data.AuthService
import com.victor.isotronalmacen.data.DataBaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val db:DataBaseService,private val authService:AuthService):ViewModel() {

    private lateinit var user: User
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState :StateFlow<HomeUiState> = _uiState

    fun getUserName(){
        viewModelScope.launch {
            db.recoverDataActualUser(authService.getCurrentUser()?.email.toString())
            user= ActualUser.getActualUser()
            _uiState.update { state->
                state.copy(username = user.name.toString())
            }
        }
    }

    fun logOut(navigateToLogin:()->Unit){
        if(Connectivity.connectOk()==true){
            if(authService.logout()){
                ActualUser.resetActualUser()
                navigateToLogin()
            }
        }else{
            _uiState.update {
                it.copy(showConnectivityAlert = true)
            }

        }
    }
    fun hideDialogAlert(){
        _uiState.update {
            it.copy(showConnectivityAlert = false)
        }
    }
    fun closeApp(){
        val activity = Connectivity.getContext() as MainActivity
       activity.finish()
    }




}


data class HomeUiState(
    val username:String="",
    val credentials:String ="",
    val showConnectivityAlert:Boolean=false
)