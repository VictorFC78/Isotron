package com.victor.isasturalmacen.viewModels.tools

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants
import com.victor.isasturalmacen.data.DataBaseService
import com.victor.isasturalmacen.domain.KindOfTool
import com.victor.isasturalmacen.domain.Tool
import com.victor.isotronalmacen.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.C
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddToolViewModel @Inject constructor(private val db: DataBaseService, private val authService: AuthService):ViewModel() {

    private val _uiState = MutableStateFlow(AddToolUiState())
    val uiState :StateFlow<AddToolUiState> = _uiState
init {
    getListOfKindTools()
}
    private fun getListOfKindTools(){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                async {
                    db.getListKindOfTools()
                }
            }
            val list =result.await().let { lst->
                lst.map { enlist->enlist.kind }
            }
            _uiState.update { it.copy(listOfKidOfTools = list) }
        }
    }

    fun hideDialogs(){
        _uiState.update {
            it.copy(showHelpDialog = false, showExitDialog = false,
                showErrorDialog = false, showConnectivityOk = false, showMenu = false,
                userName = ActualUser.getActualUser().name!!, showMenuNewKindOfTool = false)
        }
    }
    fun onChangedValues(description:String,pricePerDay:String){
        _uiState.update {
            it.copy(description = description,pricePerDay=pricePerDay)
        }
        allFieldsIsFilled()
    }
    fun addNewKindOfTool(){
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                if(_uiState.value.listOfKidOfTools.none {
                        it == _uiState.value.newKindOfTool.uppercase()
                    }){
                    val result = withContext(Dispatchers.IO){
                        async {
                            db.addNewKindOfTool(KindOfTool(_uiState.value.newKindOfTool.uppercase()))
                        }
                    }
                    result.await()
                    getListOfKindTools()
                    hideDialogs()
                    Toast.makeText(Connectivity.getContext(),"Se ha añadido a la lista",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(Connectivity.getContext(),"Ya esta en la lista",Toast.LENGTH_LONG).show()
                }
            }
        }else{
            _uiState.update { it.copy(showConnectivityOk = true) }
        }
    }

    fun onChangedValueNewKinOfTool(kindOfTool:String){
        _uiState.update { it.copy(newKindOfTool =kindOfTool ) }
    }
    private fun allFieldsIsFilled(){
        _uiState.update { it.copy(enableButtonAdd =_uiState.value.kindOfToolSelected.isNotBlank()
                &&_uiState.value.pricePerDay.isNotBlank()
                && _uiState.value.description.isNotBlank() ) }
    }

    //añade una herramienta a la base de datos
    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewTool(){
        if(Connectivity.connectOk()==true){
            if(checkCorrectPrice()){
                try {
                    viewModelScope.launch {
                        //falta comprobar si ya la herramienta ya existe hay que recuperra todoas las herramientas y comprobar si existe
                        val tool =getProvisionalTool()
                        val resul= withContext(Dispatchers.IO){
                            async {
                                existTool(tool)
                            }
                        }
                        if(resul.await()){
                            db.addNewTool(tool)
                            Toast.makeText(Connectivity.getContext(),"Se ha añadido la herramienta",Toast.LENGTH_LONG).show()
                            deleteData()
                        }else{
                            Toast.makeText(Connectivity.getContext(),"La herramienta ya existe",Toast.LENGTH_LONG).show()
                        }
                    }
                }catch (e:Exception){

                    deleteData()
                }
            }else{
                _uiState.update { it.copy(showHelpDialog = true) }
            }
        }else{
            _uiState.update {
                it.copy(showConnectivityOk = true)
            }
            deleteData()
        }

    }
    private suspend fun existTool(tool:Tool):Boolean{
        return db.getAllTools().none { it->it.id == tool.id }

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
        return try {
            if(_uiState.value.pricePerDay.contains('.')){
                _uiState.value.pricePerDay.toDouble()
                true
            } else {
                _uiState.value.pricePerDay.toInt()
                true
            }
        }catch (e:NumberFormatException){
            false
        }
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
    fun showMenuNewKindOfTool()  {
        _uiState.update { it.copy(showMenuNewKindOfTool = true) }
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
    val showMenuNewKindOfTool:Boolean=false,
    val userName:String="",
    val newKindOfTool:String="",
    val listOfKidOfTools:List<String> = listOf(),
    val enableButtonAdd:Boolean=false
)