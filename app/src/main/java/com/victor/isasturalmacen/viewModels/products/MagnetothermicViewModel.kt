package com.victor.isotronalmacen.viewmodels.products

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.WriteFiles
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants
import com.victor.isasturalmacen.data.Constants.MAGNETOS
import com.victor.isasturalmacen.data.DataBaseService
import com.victor.isasturalmacen.domain.Product
import com.victor.isasturalmacen.domain.RegisterDeleteProduct
import com.victor.isasturalmacen.domain.RegisterStockProduct
import com.victor.isotronalmacen.data.AuthService

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class MagnetothermicViewModel @Inject constructor(private val authService: AuthService, private val db: DataBaseService):ViewModel() {


    private val user = ActualUser.getActualUser()
    private val _uiState = MutableStateFlow(MagnetothermicUiState())
    val uiState : StateFlow<MagnetothermicUiState> = _uiState

    init {
        viewModelScope.launch {
            db.getFlowListOfProductByCollection(MAGNETOS).collect(){
                    magnetos->
                _uiState.update {
                    it.copy(user = user.name!!, listOfMagnetothermic =magnetos,
                        userCredentials = user.credentials!! == "admin")
                }
            }
        }
    }


    fun onClickInputOutputMagneto(product: Product, inOut: Boolean) {
        _uiState.update {
            it.copy(magnetothermic = product, inOut = inOut, showDialogAmount = true)
        }
    }

    fun onValueChanged(amount: String) {
        _uiState.update {
            it.copy(inOutAmount = amount)
        }
    }

    private fun correctAmount(): Boolean {
        return _uiState.value.magnetothermic.stockActual >= _uiState.value.inOutAmount.toInt()
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
    private fun getRegisterStockContactor(): RegisterStockProduct {
        return RegisterStockProduct(user = _uiState.value.user, dateIn = actualDateTime(),
            id = _uiState.value.magnetothermic.id, refProduct = _uiState.value.magnetothermic.refProduct,
            outAmount = _uiState.value.inOutAmount.toInt())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRegisterDeleteProduct(): RegisterDeleteProduct {
        return RegisterDeleteProduct(id=_uiState.value.magnetothermic.id, stockActual = _uiState.value.magnetothermic.stockActual.toInt(),
            manufacturer = _uiState.value.magnetothermic.manufacturer, refProduct = _uiState.value.magnetothermic.refProduct,
            date = actualDateTime(), user = ActualUser.getActualUser().name!!
        )
    }


    fun hideDialogAmount() {
        _uiState.update { it.copy(showDialogAmount = false) }
    }

    fun hideDialogError() {
        _uiState.update { it.copy(showDialogError = false) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInputOutAmountMagnetos() {
        if(correctFormatAmount()!=0){
            when{
                _uiState.value.inOut->{
                    getInputOutputScope(_uiState.value.magnetothermic.stockActual + _uiState.value.inOutAmount.toInt())
                }
                !_uiState.value.inOut && correctAmount()->{
                    getInputOutputScope(_uiState.value.magnetothermic.stockActual - _uiState.value.inOutAmount.toInt())
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
                db.updateStockProduct(kindOfCollection = Constants.MAGNETOS, id=_uiState.value.magnetothermic.id, amount =amount)
                db.createRegisterStockProduct(kindOfCollection =Constants. MAGNETOSINPUTS_OUTPUTS, registerStockProduct = getRegisterStockContactor())
                db.getFlowListOfProductByCollection(MAGNETOS).collect(){magnetos->
                    _uiState.update {
                        it.copy(listOfMagnetothermic = magnetos, showDialogAmount = false)
                    }
                }
            }
        }else{
            showDialogErrorConnectivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteMagneto() {
        if (Connectivity.connectOk()==true){
            viewModelScope.launch {
                db.deleteproductById(kindOfCollection =Constants.MAGNETOS, id = _uiState.value.magnetothermic.id)
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
    fun downloadAllMagnetos() {
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                val file = WriteFiles.createNewFileInDirectory(Constants.MAGNETOS)
                if(file!=null){
                    WriteFiles.writeDataInFile(file,_uiState.value.listOfMagnetothermic)
                }
            }
        }else{
            showDialogErrorConnectivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadInputOutputMagnetos() {
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                val file = WriteFiles.createNewFileInDirectory("CONTACTORSINPUT_OUTPUT")
                if(file!=null){
                    val listRegister = db.getListRegisterStockProducts(Constants.MAGNETOSINPUTS_OUTPUTS)
                    WriteFiles.writeDataInFile(file,listRegister)
                }
            }
        }else{
            showDialogErrorConnectivity()
        }
    }

    fun hideDialogDownloadFiles() {
        _uiState.update { it.copy(showDialogDownloadFiles = false) }
    }

    fun onClickDeleteMagneto(item: Product) {
        _uiState.update {
            it.copy(magnetothermic = item, showDialogDelete = true)
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

}

data class MagnetothermicUiState(val listOfMagnetothermic:List<Product> = emptyList(),
                                 val magnetothermic: Product = Product(),
                                 val showDialogAmount:Boolean=false,
                                 val showDialogDelete:Boolean=false,
                                 val showDialogError:Boolean=false,
                                 val showDialogDownloadFiles:Boolean=false,
                                 val inOutAmount:String="",
                                 val inOut:Boolean=true,
                                 val messageError:String="",
                                 val user:String="",
                                 val userCredentials:Boolean=false)