package com.victor.isasturalmacen.viewModels.products

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.WriteFiles
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants
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
import org.checkerframework.checker.units.qual.C
import javax.inject.Inject

@HiltViewModel
class WiresViewModel @Inject constructor(private val db: DataBaseService, private val authService: AuthService) :ViewModel(){

    private val _uiState = MutableStateFlow(WireFlowUiState())
    private val user = ActualUser.getActualUser()
    val uiState :StateFlow<WireFlowUiState> = _uiState


    init {
        viewModelScope.launch {
            db.getFlowListOfProductByCollection(Constants.WIRES).collect(){wires->
                _uiState.update {
                    it.copy(listOfWires = wires, user = user.name!!,
                        userCredentials = user.credentials!! == "admin")
                }
            }
        }
    }

    fun showAmountInputOutputWire(item: Product, inOut:Boolean) {
        _uiState.update {
            it.copy(showDialogAmount = true, product = item, inOrOut = inOut)
        }

    }


    private fun formatCorrectAmount( ):Int {
        return try {
           val value = _uiState.value.amount.toInt()
            if(value > 0) value
            else 0
        }catch (e:NumberFormatException){
            0
        }
    }

    private fun correctAmount(): Boolean {
        return _uiState.value.product.stockActual >= _uiState.value.amount.toInt()
    }

    fun hideDialog(){
        _uiState.update {
            it.copy(showDialogAmount = false, showDialogError = false, showDeleteDialog = false,
                showDialogDownloadFiles = false)
        }
    }

    fun changeValues(value: String) {
        _uiState.update {
            it.copy(amount = value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInputOutputAmount() {
       if(formatCorrectAmount() !=0){
           when{
               !_uiState.value.inOrOut && correctAmount()->{
                  getInputOutputScope(_uiState.value.product.stockActual - _uiState.value.amount.toInt())
               }
              uiState.value.inOrOut->{
                  getInputOutputScope(_uiState.value.product.stockActual + _uiState.value.amount.toInt())
               }else->{
               showErrorDialogUnit()
               }
           }
       }else{
          showErrorDialogUnit()
       }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun  getInputOutputScope(amount: Int) {
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                db.updateStockProduct(kindOfCollection = Constants.WIRES,id=_uiState.value.product.id, amount = amount)
                db.createRegisterStockProduct(kindOfCollection = Constants.WIRESINPUTS_OUTPUTS, registerStockProduct = getRegisterStockWire())
                db.getFlowListOfProductByCollection(Constants.WIRES).collect(){wires->
                    _uiState.update {
                        it.copy(listOfWires = wires, showDialogAmount = false)
                    }
                }
            }
        }else{
           showErrorDialogConnectivity()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRegisterStockWire(): RegisterStockProduct {
        return RegisterStockProduct(user = _uiState.value.user, dateIn = actualDateTime(),
            id = _uiState.value.product.id, refProduct = _uiState.value.product.refProduct,
            outAmount = _uiState.value.amount.toInt())
    }

    fun logOut(navigateToLogin:()->Unit){
        if(Connectivity.connectOk()==true){
            if(authService.logout()){
                ActualUser.resetActualUser()
                navigateToLogin()
            }
        }else{
           showErrorDialogConnectivity()
        }
    }

    fun showDialogDelete(item:Product){
        _uiState.update {
            it.copy(showDeleteDialog = true, product = item)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteWireProduct(){
        if (Connectivity.connectOk()==true){
            viewModelScope.launch {
                db.deleteproductById(kindOfCollection = Constants.WIRES,id=_uiState.value.product.id)
                db.addRegisterDeleteProduct(createRegisterDeleteProduct())
                hideDialog()
            }
        }else{
            showErrorDialogConnectivity()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRegisterDeleteProduct(): RegisterDeleteProduct {
        return RegisterDeleteProduct(id=_uiState.value.product.id, stockActual = _uiState.value.product.stockActual.toInt(),
            manufacturer = _uiState.value.product.manufacturer, refProduct = _uiState.value.product.refProduct,
            date = actualDateTime(), user = ActualUser.getActualUser().name!!
        )
    }
   @RequiresApi(Build.VERSION_CODES.O)
   fun downloadAllWires(){
       if(Connectivity.connectOk()==true){
           viewModelScope.launch {
               val file =WriteFiles.createNewFileInDirectory(Constants.WIRES)
               if(file!=null){
                   WriteFiles.writeDataInFile(file,_uiState.value.listOfWires)
               }
           }
       }else{
          showErrorDialogConnectivity()
       }
   }
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadInputOutputWire(){
        if(Connectivity.connectOk()==true){
            viewModelScope.launch {
                val file =WriteFiles.createNewFileInDirectory("WIRESINPUT_OUTPUT")
                if(file!=null){
                    val listRegister = db.getListRegisterStockProducts(Constants.WIRESINPUTS_OUTPUTS)
                    WriteFiles.writeDataInFile(file,listRegister)
                }
            }
        }else{
            showErrorDialogConnectivity()
        }
    }
    private fun showErrorDialogUnit(){
        _uiState.update {
            it.copy(showDialogError = true, messageError = "Revise la Cantidad")
        }
    }
    private fun showErrorDialogConnectivity(){
        _uiState.update {
            it.copy(showDialogError = true, messageError = "Revise la conectividad")
        }
    }
}


data class WireFlowUiState(val listOfWires:List<Product> = emptyList(),
                           val connectivityOK:Boolean =false,
                           val user:String="",
                           val showDialogAmount:Boolean=false,
                           val product:Product=Product(),
                           val amount:String="",
                           val inOrOut:Boolean=false,
                           val showDialogError:Boolean=false,
                           val showDeleteDialog:Boolean=false,
                           val showDialogDownloadFiles:Boolean=false,
                           val messageError:String="",
                           val userCredentials:Boolean=false)