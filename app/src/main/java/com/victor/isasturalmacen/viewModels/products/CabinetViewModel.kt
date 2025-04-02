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
import com.victor.isasturalmacen.data.Constants.CABINETS
import com.victor.isasturalmacen.data.Constants.CABINETSINPUTS_OUTPUTS
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
class CabinetViewModel @Inject constructor(private val authService: AuthService,private val db: DataBaseService):ViewModel() {


    private val user = ActualUser.getActualUser()
    private val _uiState = MutableStateFlow(CabinetUiState())
    val uiState : StateFlow<CabinetUiState> = _uiState

    init {
        viewModelScope.launch {
            db.getFlowListOfProductByCollection(CABINETS).collect(){
                    cabinets->
                _uiState.update {
                    it.copy(user = user.name!!, userCredentials = user.credentials!! == "admin",listOfCabinet =cabinets )
                }
            }
        }
    }

    fun onClickInputOutputCabinet(product: Product, inOut: Boolean) {
        _uiState.update {
            it.copy(cabinet = product, inOut = inOut, showDialogAmount = true)
        }
    }

    fun onValueChanged(amount: String) {
        _uiState.update {
            it.copy(inOutAmount = amount)
        }
    }

    private fun correctAmount(): Boolean {
        return _uiState.value.cabinet.stockActual >= _uiState.value.inOutAmount.toInt()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRegisterStockCabinet(): RegisterStockProduct {
        return RegisterStockProduct(user = _uiState.value.user, dateIn = actualDateTime(),
            id = _uiState.value.cabinet.id, refProduct = _uiState.value.cabinet.refProduct,
            outAmount = _uiState.value.inOutAmount.toInt())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRegisterDeleteProduct(): RegisterDeleteProduct {
        return RegisterDeleteProduct(id=_uiState.value.cabinet.id, stockActual = _uiState.value.cabinet.stockActual.toInt(),
            manufacturer = _uiState.value.cabinet.manufacturer, refProduct = _uiState.value.cabinet.refProduct,
            date = actualDateTime(), user = ActualUser.getActualUser().name!!)
    }

    fun hideDialogAmount() {
        _uiState.update { it.copy(showDialogAmount = false) }
    }

    fun hideDialogError() {
        _uiState.update { it.copy(showDialogError = false) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInputOutAmountCabinet() {
        if(correctFormatAmount()!=0){
            when{
                _uiState.value.inOut->{
                    getInputOutputScope(_uiState.value.cabinet.stockActual + _uiState.value.inOutAmount.toInt())
                }
                !_uiState.value.inOut && correctAmount()->{
                    getInputOutputScope(_uiState.value.cabinet.stockActual - _uiState.value.inOutAmount.toInt())
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
                db.updateStockProduct(kindOfCollection = CABINETS, id=_uiState.value.cabinet.id, amount =amount)
                db.createRegisterStockProduct(kindOfCollection = CABINETS, registerStockProduct = getRegisterStockCabinet())
                db.getFlowListOfProductByCollection(CABINETS).collect(){cabinet->
                    _uiState.update {
                        it.copy(listOfCabinet = cabinet, showDialogAmount = false)
                    }
                }
            }
        }else{
            showDialogErrorConnectivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteCabinet() {
        if (Connectivity.connectOk()==true){
            viewModelScope.launch {
                db.deleteproductById(kindOfCollection = CABINETS, id = _uiState.value.cabinet.id)
                db.addRegisterDeleteProduct(createRegisterDeleteProduct())
                hideDialogDelete()
            }
        }else{
            showDialogErrorConnectivity()
        }
    }

    fun hideDialogDelete() {
        _uiState.update { it.copy(showDialogDelete = false) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadAllCabinet() {
        if(Connectivity.connectOk()==true){
            var ok = false
            viewModelScope.launch {
                val deferred = withContext(Dispatchers.IO){
                    async {
                        val file = WriteFiles.createNewFileInDirectory(CABINETS)
                        if(file!=null){
                         ok =  WriteFiles.writeDataInFile(file,_uiState.value.listOfCabinet)
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
    fun downloadInputOutputCabinet() {
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                var ok = false
                val deferred = withContext(Dispatchers.IO){
                    async {
                        val file = WriteFiles.createNewFileInDirectory(CABINETSINPUTS_OUTPUTS)
                        if(file!=null){
                            val listRegister = db.getListRegisterStockProducts(CABINETSINPUTS_OUTPUTS)
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

    fun hideDialogDownloadFiles() {
        _uiState.update { it.copy(showDialogDownloadFiles = false) }
    }

    fun onClickDeleteCabinet(item: Product) {
        _uiState.update {
            it.copy(cabinet = item, showDialogDelete = true)
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

    fun showDialogDownloadFiles() {
        _uiState.update {
            it.copy(showDialogDownloadFiles = true)
        }
    }
    private fun showToast(text:String){
        Toast.makeText(Connectivity.getContext(),text, Toast.LENGTH_LONG).show()
    }
}
data class CabinetUiState(val listOfCabinet:List<Product> = emptyList(),
                              val cabinet: Product = Product(),
                              val showDialogAmount:Boolean=false,
                              val showDialogDelete:Boolean=false,
                              val showDialogError:Boolean=false,
                              val showDialogDownloadFiles:Boolean=false,
                              val inOutAmount:String="",
                              val inOut:Boolean=true,
                              val messageError:String="",
                              val user:String="",
                              val userCredentials:Boolean=false)