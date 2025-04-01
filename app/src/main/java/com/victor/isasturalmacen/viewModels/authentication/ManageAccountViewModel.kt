package com.victor.isasturalmacen.viewModels.authentication

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.MainActivity
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.WriteFiles
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants
import com.victor.isasturalmacen.domain.User
import com.victor.isotronalmacen.data.AuthService
import com.victor.isotronalmacen.data.DataBaseService
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
class ManageAccountViewModel @Inject constructor(private val authService: AuthService,
                                                 private val db:DataBaseService):ViewModel() {

    private val _uiState = MutableStateFlow(ManageAccountUiState())
    val uiState: StateFlow<ManageAccountUiState> = _uiState

    init {
        _uiState.update { it.copy(user = ActualUser.getActualUser()) }
    }

    fun onAllValuesChanged(newPassword: String, repeatNewPassword: String) {
        _uiState.update { state ->
            state.copy(newPassword = newPassword, repeatNewPassword = repeatNewPassword)
        }
        formIsValid()
    }

    private fun formIsValid() {
        when {
            _uiState.value.newPassword.length == 11
                    && _uiState.value.newPassword == _uiState.value.repeatNewPassword -> {
                _uiState.update { state ->
                    state.copy(formIsValid = true)
                }
            }

            else -> {
                _uiState.update { state ->
                    state.copy(formIsValid = false)

                }
            }
        }
    }

    fun logOut(navigateToLogin: () -> Unit) {
        if (Connectivity.connectOk() == true) {
            if (authService.logout()) {
                navigateToLogin()
            } else {
                //mostramos un Dialogo
            }
        } else {
            _uiState.update {
                it.copy(showConnectivityOk = true)
            }
        }

    }

    fun onClickChangePassword() {
        _uiState.update { state ->
            state.copy(showInputData = !_uiState.value.showInputData)
        }

    }

    fun changePassword(newPassword: String) {

        if (Connectivity.connectOk() == true) {
            viewModelScope.launch {
                try {
                    val result = withContext(Dispatchers.IO) {
                        async { authService.changePassword(newPassword) }
                    }
                    onClickChangePassword()
                    updateMessageToast("Contraseña Cambiada")
                } catch (e: Exception) {
                    updateMessageToast("Se ha producido un error")
                }
            }
        } else {
            _uiState.update {
                it.copy(showConnectivityOk = true)
            }
        }

    }

    fun closeApp() {
        val activity = Connectivity.getContext() as MainActivity
        activity.finish()
    }

    private fun connectivityOk(context: Context): Boolean {
        val con = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return con.activeNetworkInfo?.isConnected ?: false
    }

    private fun updateMessageToast(message: String) {
        _uiState.update { state ->
            state.copy(messageToast = message, showToast = true)
        }
    }

    fun hideToastAndDialog() {
        _uiState.update { state ->
            state.copy(showToast = false, showConnectivityOk = false, showDialogDownload = false)
        }
    }

    fun showDialogDownload() {
        _uiState.update { it.copy(showDialogDownload = true) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadRegisterToolsByActualUser() {
        if (Connectivity.connectOk() == true) {
            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) {
                    db.getAllRegisterInputAndOutputsTools().filter { register ->
                        register.user.equals(_uiState.value.user.name)
                    }
                }
                WriteFiles.createNewFileInDirectory(Constants.TOOLINPUTS_OUTPUTS).let { file ->
                    if (file != null) WriteFiles.writeDataInFile(file, result)
                    hideToastAndDialog()
                    Toast.makeText(
                        Connectivity.getContext(),
                        "Se ha descargado el archivo",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(Connectivity.getContext(), "Se ha producido un error", Toast.LENGTH_LONG)
                .show()
        }
    }

//me falta por acabar
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadRegisterProductsByActualUser() {
    if (Connectivity.connectOk() == true) {

    }
}
}
data class ManageAccountUiState(
    val newPassword:String ="",
    val repeatNewPassword:String ="",
    val showInputData:Boolean = false,
    val user:User= User(),
    val showDialogDownload:Boolean=false,
    val formIsValid:Boolean =false,
    val showToast:Boolean = false,
    val messageToast:String ="",
    val showConnectivityOk:Boolean=false)
