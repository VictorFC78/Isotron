package com.victor.isasturalmacen.viewModels.tools

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.domain.Tool
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
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddToolViewModel @Inject constructor(private val db:DataBaseService,private val authService: AuthService):ViewModel() {

    private val _uiState = MutableStateFlow(AddToolUiState())
    val uiState :StateFlow<AddToolUiState> = _uiState


    fun hideDialogs(){
        _uiState.update {
            it.copy(showHelpDialog = false, showExitDialog = false,
                showErrorDialog = false, showConnectivityOk = false, showMenu = false,
                userName = ActualUser.getActualUser().name!!)
        }
    }
    fun onChangedValues(description:String,pricePerDay:String){
        _uiState.update {
            it.copy(description = description,pricePerDay=pricePerDay)
        }
    }
    private fun checkValuesAreCorrect(): Boolean {
        return _uiState.value.kindOfToolSelected.isNotBlank()
                && checkCorrectPrice()
                && _uiState.value.description.isNotBlank()

    }

    //añade una herramienta a la base de datos
    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewTool(){
        if(Connectivity.connectOk()==true){
            if(checkValuesAreCorrect()){
                try {
                    viewModelScope.launch {
                        //falta comprobar si ya la herramienta ya existe hay que recuperra todoas las herramientas y comprobar si existe
                        val tool =getProvisionalTool()
                        val resul= withContext(Dispatchers.IO){
                            async {
                                db.addNewTool(tool)
                            }
                        }
                        resul.isCompleted.let {
                            _uiState.update { it.copy(showExitDialog = true) }
                            deleteData()
                        }
                    }
                }catch (e:Exception){
                    _uiState.update { it.copy(showErrorDialog = true) }
                    deleteData()
                }
            }else{
                _uiState.update {
                    it.copy(showHelpDialog = true)
                }
            }
        }else{
            _uiState.update {
                it.copy(showConnectivityOk = true)
            }
            deleteData()
        }

    }
    private fun deleteData(){
        _uiState.update {
            it.copy(kindOfToolSelected = "", description = "", pricePerDay = "")
        }
    }
    private fun getNewToolId(): String {
        return _uiState.value.kindOfToolSelected.plus("_".plus(randomId()))
    }

    private fun checkCorrectPrice(): Boolean {
        val regex = Regex("\\d{1,2},\\d{1,2}|\\d{1,2}\\.\\d{1,2}")
        return _uiState.value.pricePerDay.uppercase(Locale.ROOT).matches(regex)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getProvisionalTool(): Tool {
        val date = actualDateTime()
        return Tool(id =getNewToolId(),
            description = _uiState.value.description,
            pricePerDay = _uiState.value.pricePerDay.replace(oldChar = ',', newChar = '.').toDouble(),
            chargeDay = date)
    }
    fun logout(navigateToLogin:()->Unit){
        if(Connectivity.connectOk()==true){
            if(authService.logout()){
                ActualUser.resetActualUser()
                navigateToLogin()
            }
        }else{
            _uiState.update {
                it.copy(showConnectivityOk = true)
            }
        }
    }

    fun showMenu(){
        _uiState.update { it.copy(showMenu = true) }
    }

    fun kindOfToolSelected(kindOfTool: String) {
        _uiState.update { it.copy(kindOfToolSelected = kindOfTool) }
    }
    private fun randomId() = (0..1000).random()
}
data class AddToolUiState (
    val kindOfToolSelected:String="",
    val description:String="",
    val pricePerDay:String="",
    val showHelpDialog:Boolean =false,
    val showExitDialog:Boolean=false,
    val showErrorDialog:Boolean=false,
    val showConnectivityOk:Boolean=false,
    val showMenu:Boolean=false,
    val userName:String="")