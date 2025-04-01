package com.victor.isasturalmacen.viewModels.tools

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.MainActivity
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.WriteFiles
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants
import com.victor.isasturalmacen.domain.DeleteToolLog
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
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ToolFlowViewModel @Inject constructor(private val db:DataBaseService,private val authService: AuthService):ViewModel() {

    
    private val _uiState = MutableStateFlow(ToolFlowUiState())
    val uiState : StateFlow<ToolFlowUiState> = _uiState

    init {
        viewModelScope.launch {
            getAllTools()
        }

    }
    private suspend fun getAllTools(){
            db.getAllToolsFlow().collect{ tools->
                _uiState.update {
                    it.copy(listOfTools = tools, userName= ActualUser.getActualUser().name!!,
                        userCredentianls = ActualUser.getActualUser().credentials!!)
            }
        }
    }
    //cierra la aplicacion
    fun closeApp(){
        val activity = Connectivity.getContext() as MainActivity
        activity.finish()
    }

    //cierra sesion
    fun logout(navigateToLogin:()->Unit){
        if(Connectivity.connectOk()==true){
            if(authService.logout()){
                ActualUser.resetActualUser()
                navigateToLogin()
            }
        }else{
            showDialogConnectivity()
        }

    }
    //oculta los dialogos
    fun hideDialog() {
        _uiState.update {
            it.copy(connectivityOk = false, showDownLoadInfoDialog = false,
                showDeleteDialog = false)
        }
    }
//muestra dialogo
    fun showDownLoadInfoDialog() {
        _uiState.update {
            it.copy(showDownLoadInfoDialog = true)
        }
    }

//genera un archivo csv con todas las herramientas
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadAllTools() {
        if(Connectivity.connectOk()==true){
            viewModelScope.launch{
                var ok =false
                val deferred = withContext(Dispatchers.IO){
                    async {
                        val file:File? = WriteFiles.createNewFileInDirectory(Constants.TOOL_DIRECTORY)
                        if(file!=null){
                            ok =WriteFiles.writeDataInFile(file,_uiState.value.listOfTools)
                        }
                    }
                }
                deferred.await()
                if(ok){
                    Toast.makeText(Connectivity.getContext(),"Se ha descargado el archivo",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(Connectivity.getContext(),"Se ha producido un error",Toast.LENGTH_LONG).show()

                }
                hideDialog()
            }
        }else{
            hideDialog()
            showDialogConnectivity()
        }


    }
//genera un archivo con los movimientos de las herramientas
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadAllRegisterInputsOutputTools(){
        if(Connectivity.connectOk()==true){
            viewModelScope.launch{
                var ok =false
                val deferred = withContext(Dispatchers.IO){
                    async {
                        val file:File? =WriteFiles.createNewFileInDirectory(Constants.INPUT_OUTPUT_DIRECTORY)
                        if (file != null) {
                            val list = db.getAllRegisterInputAndOutputsTools()
                            ok = WriteFiles.writeDataInFile(file,list)
                        }
                    }
                }
                deferred.await()
                if(ok)Toast.makeText(Connectivity.getContext(),"Se ha descargado el archivo",Toast.LENGTH_LONG).show()
                else Toast.makeText(Connectivity.getContext(),"Se ha producido un error",Toast.LENGTH_LONG).show()
                hideDialog()
            }
        }else{
            hideDialog()
            showDialogConnectivity()
        }

    }
    //genera un archivo de todas la herramientas que se hayan borrado
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadAllRegisterDeleteTolls(){
        if(Connectivity.connectOk()==true){
            viewModelScope.launch{
                var ok = false
                val deferred = withContext(Dispatchers.IO){
                    async {
                        val file:File? =WriteFiles.createNewFileInDirectory(Constants.DELETE_TOOL_DIRECTORY)
                        if (file != null) {
                            val list =db.getAllToolDeleteRegister()
                            ok = WriteFiles.writeDataInFile(file,list)
                        }
                    }
                }
                deferred.await()
                if(ok)Toast.makeText(Connectivity.getContext(),"Se ha descargado el archivo",Toast.LENGTH_LONG).show()
                else Toast.makeText(Connectivity.getContext(),"Se ha producido un error",Toast.LENGTH_LONG).show()
                hideDialog()
            }
        }else{
            hideDialog()
            showDialogConnectivity()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun takeOutAndReturnTool(tool: Tool, inOut: Boolean) {
        if(Connectivity.connectOk()==true){
            try {
                viewModelScope.launch {
                    val result = withContext(Dispatchers.IO){
                        async {
                            db.updateDateTool(tool.id,inOut)
                            db.addTooLoginAndToolLogout(inOut,
                                Tool(id=tool.id, description = tool.description,)
                            )
                        }
                    }
                    result.await()
                    getAllTools()
                }
            }catch (e:Exception){
               showDialogError()
            }
        }else{
            showDialogConnectivity()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteTool() {
        if(Connectivity.connectOk()==true){
            try {
                viewModelScope.launch {
                    val result = withContext(Dispatchers.IO){
                        async {
                            db.deleteTool(_uiState.value.toolSelected.id!!)
                            db.addDeleteToolRegister(DeleteToolLog(user = _uiState.value.userName,
                                idTool = _uiState.value.toolSelected.id!!, chargeDay = _uiState.value.toolSelected.chargeDay!!,
                                dischargeDay = actualDateTime(), description = _uiState.value.toolSelected.description!!,
                                pricePerDay = _uiState.value.toolSelected.pricePerDay!!)
                            )
                        }
                    }
                    result.await()
                    hideDialog()
                    getAllTools()
                }
            }catch (e:Exception){
                showDialogError()
            }
        }else{
            showDialogConnectivity()
        }
    }

    fun showDeleteDialog(tool:Tool) {
        _uiState.update { it.copy(toolSelected = tool, showDeleteDialog = true) }
    }

    private fun showDialogConnectivity(){
        _uiState.update {
            it.copy(connectivityOk = true, messageAlert = "Prohibida acción, 'Sin Conexion'", tipoAlert = "SIN CONEXION")
    }
}
    private fun showDialogError(){
        _uiState.update {
            it.copy(connectivityOk = true, messageAlert = "Se ha producido un error", tipoAlert = "ERROR")
        }
    }

}

data class ToolFlowUiState(
    val listOfTools: List<Tool> = emptyList(),
    val connectivityOk:Boolean=false,
    val showDownLoadInfoDialog:Boolean=false,
    val userName: String ="",
    val userCredentianls:String="",
    val showDeleteDialog:Boolean=false,
    val toolSelected:Tool=Tool(),
    val messageAlert:String="",
    val tipoAlert:String="")