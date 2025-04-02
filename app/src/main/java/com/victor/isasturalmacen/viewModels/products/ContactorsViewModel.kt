package com.victor.isasturalmacen.viewModels.products

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.WriteFiles
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants.CONTACTORS
import com.victor.isasturalmacen.data.Constants.CONTACTORSINPUTS_OUTPUTS
import com.victor.isasturalmacen.data.DataBaseService
import com.victor.isasturalmacen.domain.Product
import com.victor.isasturalmacen.domain.RegisterDeleteProduct
import com.victor.isasturalmacen.domain.RegisterStockProduct
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
class ContactorsViewModel @Inject constructor(private val authService: AuthService,private val db: DataBaseService):ViewModel(){


    private val user = ActualUser.getActualUser()
    private val _uiState = MutableStateFlow(ContactorsUiState())
    val uiState : StateFlow<ContactorsUiState> = _uiState

    init{
        viewModelScope.launch {
            db.getFlowListOfProductByCollection(CONTACTORS).collect(){
                contactors->
                _uiState.update {
                    it.copy(listOfContactor = contactors, user = user.name!!,
                        userCredentials = user.credentials!! == "admin"
                    )
                }
            }
        }
    }
    //actualiza el campo de texto
    fun onValueChanged(amount:String){
        _uiState.update {it.copy(inOutAmount = amount) }
    }
    //actuliza los datos del item seleccionado y muestra el dialogo para insertar cantidad
   fun onClickInputOutputContactor(item: Product, inOut:Boolean){
        _uiState.update {
            it.copy(contactor = item, showDialogAmount = true, inOut = inOut)
        }
   }
    //recupera la cantidad del editext y realiza la entrada o salida de materiañl
    @RequiresApi(Build.VERSION_CODES.O)
    fun getInputOutAmountContactors(){
        if(correctFormatAmount()!=0){
            when{
                _uiState.value.inOut->{
                    getInputOutputScope(_uiState.value.contactor.stockActual + _uiState.value.inOutAmount.toInt())
                }
                !_uiState.value.inOut && correctAmount()->{
                    getInputOutputScope(_uiState.value.contactor.stockActual - _uiState.value.inOutAmount.toInt())
                }else->{
                showDialogErrorFormat()
            }
            }
        }else{
           showDialogErrorFormat()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInputOutputScope(amount: Int) {
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                db.updateStockProduct(kindOfCollection = CONTACTORS, id=_uiState.value.contactor.id, amount =amount)
                db.createRegisterStockProduct(kindOfCollection = CONTACTORSINPUTS_OUTPUTS, registerStockProduct = getRegisterStockContactor())
                db.getFlowListOfProductByCollection(CONTACTORS).collect(){contactors->
                    _uiState.update {
                        it.copy(listOfContactor = contactors, showDialogAmount = false)
                    }
                }
            }
        }else{
            showDialogErrorConnectivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRegisterStockContactor(): RegisterStockProduct {
        return RegisterStockProduct(user = _uiState.value.user, dateIn = actualDateTime(),
            id = _uiState.value.contactor.id, refProduct = _uiState.value.contactor.refProduct,
            outAmount = _uiState.value.inOutAmount.toInt())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteContactor(){
        if (Connectivity.connectOk()==true){
            viewModelScope.launch {
                db.deleteproductById(kindOfCollection = CONTACTORS, id = _uiState.value.contactor.id)
                db.addRegisterDeleteProduct(createRegisterDeleteProduct())
                hideDialogDelete()
            }
        }else{
            showDialogErrorConnectivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRegisterDeleteProduct(): RegisterDeleteProduct {
        return RegisterDeleteProduct(id=_uiState.value.contactor.id, stockActual = _uiState.value.contactor.stockActual.toInt(),
            manufacturer = _uiState.value.contactor.manufacturer, refProduct = _uiState.value.contactor.refProduct,
            date = actualDateTime(), user = ActualUser.getActualUser().name!!
        )
    }

    private fun correctAmount(): Boolean {
        return _uiState.value.contactor.stockActual >= _uiState.value.inOutAmount.toInt()
    }

    private fun correctFormatAmount():Int{
        return try {
            val value = _uiState.value.inOutAmount.toInt()
            if(value > 0) value
            else 0
        }catch (e:NumberFormatException){
            0
        }
    }

    //muestra el dialogo para borrar un item
    fun onClickDeleteContactor(item: Product) {
        _uiState.update {
            it.copy(contactor = item, showDialogDelete = true)
        }
    }
    private fun showDialogErrorConnectivity(){
        _uiState.update {
            it.copy(showDialogError = true, messageError = "Revise la Conexion")
        }
    }
    private fun showDialogErrorFormat(){
        _uiState.update {
            it.copy(showDialogError = true, messageError = "Revise la Cantidad")
        }
    }
    fun hideDialogError(){
        _uiState.update {
            it.copy(showDialogError = false)
        }
    }
    fun hideDialogAmount(){
        _uiState.update {
            it.copy(showDialogAmount = false)
        }
    }
    fun hideDialogDelete(){
        _uiState.update {
            it.copy(showDialogDelete = false)
        }
    }
    fun logOut(navigateToLogin:()->Unit){
        if(Connectivity.connectOk()==true){
            if(authService.logout()){
                ActualUser.resetActualUser()
                navigateToLogin()
            }
        }else{
            showDialogErrorConnectivity()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadAllContactors(){
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                var ok = false
                val deferred = withContext(Dispatchers.IO){
                    async {
                        val file = WriteFiles.createNewFileInDirectory(CONTACTORS)
                        if(file!=null){
                        ok = WriteFiles.writeDataInFile(file,_uiState.value.listOfContactor)
                        }
                    }
                }
                deferred.await()
                if (ok) showToast("Se ha descargado el archivo")
                else showToast("Se ha producido un error")
            }
        }else{
            showDialogErrorConnectivity()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadInputOutputContactors(){
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                var ok = false
                val deferred = withContext(Dispatchers.IO){
                    async {
                        val file = WriteFiles.createNewFileInDirectory(CONTACTORSINPUTS_OUTPUTS)
                        if(file!=null){
                            val listRegister = db.getListRegisterStockProducts(CONTACTORSINPUTS_OUTPUTS)
                            ok = WriteFiles.writeDataInFile(file,listRegister)
                        }
                    }
                }
                deferred.await()
                if (ok) showToast("Se ha descargado el archivo")
                else showToast("Se ha producido un error")
            }
        }else{
            showDialogErrorConnectivity()
        }
    }
    fun showDialogDownloadFiles(){
        _uiState.update {
            it.copy(showDialogDownloadFiles = true)
        }
    }
    fun hideDialogDownloadFiles(){
        _uiState.update {
            it.copy(showDialogDownloadFiles = false)
        }
    }
    private fun showToast(text:String){
        Toast.makeText(Connectivity.getContext(),text, Toast.LENGTH_LONG).show()
    }
}

data class ContactorsUiState(val listOfContactor:List<Product> = emptyList(),
                             val contactor:Product=Product(),
                             val showDialogAmount:Boolean=false,
                             val showDialogDelete:Boolean=false,
                             val showDialogError:Boolean=false,
                             val showDialogDownloadFiles:Boolean=false,
                             val inOutAmount:String="",
                             val inOut:Boolean=true,
                             val messageError:String="",
                             val user:String="",
                             val userCredentials:Boolean=false)