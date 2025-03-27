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
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants
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

    
    private val _uiState = MutableStateFlow<ToolFlowUiState>(ToolFlowUiState())
    val uiState : StateFlow<ToolFlowUiState> = _uiState

    init {
       getAllTools()
    }
    private fun getAllTools(){
        viewModelScope.launch {
            db.getAllToolsFlow().collect{ tools->
                _uiState.update {
                    it.copy(listOfTools = tools, userName= ActualUser.getActualUser().name!!,
                        userCredentianls = ActualUser.getActualUser().credentials!!)
                }
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
            _uiState.update {
                it.copy(connectivityOk = true)
            }
        }

    }
    //oculta los dialogos
    fun hideDialog() {
        _uiState.update {
            it.copy(connectivityOk = false, showDownLoadInfoDialog = false)
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

           val file =createFile(Constants.TOOL_DIRECTORY)
           viewModelScope.launch{
               var exit = false
               val deferred = withContext(Dispatchers.IO){
                   async {
                       if(file!=null){
                            exit = writeFile(_uiState.value.listOfTools,createBufferedWriter(file))
                       }
                   }
               }
              deferred.await()
               showToast(exit,Connectivity.getContext())
               hideDialog()
           }

    }
//genera un archivo con los movimientos de las herramientas
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadAllRegisterInputsOutputTools(){
            val file =createFile(Constants.INPUT_OUTPUT_DIRECTORY)
            viewModelScope.launch{
                var exit =false
                val deferred = withContext(Dispatchers.IO){
                    async {
                        if (file != null) {
                            exit= writeFile(db.getAllRegisterInputAndOutputsTools(), createBufferedWriter(file))
                        }
                    }
                }
                deferred.await()
                showToast(exit,Connectivity.getContext())
                hideDialog()
            }
    }
    //genera un archivo de todas la herramientas que se hayan borrado
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadAllRegisterDeleteTolls(){
        val file =createFile(Constants.DELETE_TOOL_DIRECTORY)
        viewModelScope.launch(Dispatchers.IO) {
            var exit = false
            val deferred = withContext(Dispatchers.IO){
                async {
                    if (file != null) {
                        writeFile(db.getAllToolDeleteRegister(), createBufferedWriter(file))
                    }
                }
            }
            deferred.await()
            showToast(exit,Connectivity.getContext())
            hideDialog()
        }
    }
// escribe datos en un csv
    private fun <T> writeFile(list:List<T>,buffer:BufferedWriter): Boolean {
            return try {
                list[0]!!::class
                    .java.declaredFields
                    .map { it.name }
                    .forEach {
                        buffer.write("${it};")
                    }
                buffer.newLine()
                list.forEach {
                    buffer.write(it.toString())
                    buffer.newLine()
                }
                buffer.flush()
                buffer.close()
                true
            }catch (e:IOException){
                false
            }

    }

    //genera un directorio y archivo para guardar datos
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFile(directory:String,): File? {
        return try {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val newDir = File(path,directory)
            if(!newDir.exists()) newDir.mkdirs()
            File(newDir,LocalDate.now().toString())
        }catch (e:FileNotFoundException){
            null
        }

    }
    private fun createBufferedWriter(file:File): BufferedWriter {
        return FileOutputStream(file).bufferedWriter()
    }
    private fun showToast(ok:Boolean,context: Context){
        return when{
            ok->Toast.makeText(context,"Archivo descargado",Toast.LENGTH_LONG).show()
            else->Toast.makeText(context,"Se ha producido un error",Toast.LENGTH_LONG).show()
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
                    db.getAllToolsFlow().collect{ tools->
                        _uiState.update {
                            it.copy(listOfTools = tools)
                        }
                    }
                }
            }catch (e:Exception){
                //mostramoe un error por pantalla
            }
        }else{
            _uiState.update {
                it.copy(connectivityOk = true)
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun takeOutTool(tool: Tool) {

    }


    fun deleteTool(dt: Tool) {

    }



}
data class ToolFlowUiState(
    val listOfTools: List<Tool> = emptyList(),
    val connectivityOk:Boolean=false,
    val showDownLoadInfoDialog:Boolean=false,
    val userName: String ="",
    val userCredentianls:String=""
)