package com.victor.isasturalmacen.viewModels.products

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.isasturalmacen.auxs.Connectivity
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.data.Constants
import com.victor.isasturalmacen.data.DataBaseService
import com.victor.isasturalmacen.domain.Product
import com.victor.isasturalmacen.domain.RegisterNewProduct
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
class NewWireViewModel @Inject constructor(private val db: DataBaseService, private val authService: AuthService):ViewModel(){

    private val _uiState = MutableStateFlow(UiStateNewWire())
    val uiState:StateFlow<UiStateNewWire> = _uiState

    init {
        _uiState.update {
            it.copy(user = ActualUser.getActualUser().name!!)
        }
    }

    fun onValuesChanged(kind:String,manufacturer:String,
                        refProduct:String,pricePerUnit:String,stockActual:String){
        _uiState.update {
            it.copy(kind = kind, manufacturer = manufacturer,
                refProduct = refProduct,pricePerUnit=pricePerUnit, stockActual = stockActual)
        }
        formIsValid()
    }

    private fun formIsValid(){
        if(_uiState.value.kind.isNotBlank() && _uiState.value.manufacturer.isNotBlank()
            && _uiState.value.refProduct.isNotBlank() && _uiState.value.pricePerUnit.isNotBlank()
            && _uiState.value.stockActual.isNotBlank()){
            _uiState.update { it.copy(enableButton = true) }
        }else {
            _uiState.update { it.copy(enableButton = false) }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewWire(navigateToWires:()->Unit){
        if(checkFormatNumber() && Connectivity.connectOk()==true){
            viewModelScope.launch {
                val deferred = withContext(Dispatchers.IO){
                    async {
                        db.getListOfProductByCollection(kindOfCollection = Constants.WIRES).none { it.id==getNewWireId() }
                    }
                }
                if (deferred.await()){
                    db.addNewProduct(kindOfCollection = Constants.WIRES, Product(id=getNewWireId(), kind = _uiState.value.kind.uppercase(),
                        manufacturer = _uiState.value.manufacturer.uppercase(),
                        refProduct = _uiState.value.refProduct.uppercase(), pricePerUnit = _uiState.value.pricePerUnit.toDouble(),
                        stockActual = _uiState.value.stockActual.toInt())
                    )
                    db.addCreateRegisterNewProduct(createRegisterNewWire())
                        navigateToWires()
                }else{
                    _uiState.update {
                        it.copy(showDialogError = true, messageDialog = "REF Cable ya existe")
                    }
                }

                }
        }else{
            _uiState.update {
                it.copy(showDialogError = true, messageDialog = "Revise la conexion\n Unidades de Precio/Cantidad")
            }
        }
    }

    private fun getNewWireId(): String {
        return _uiState.value.id.plus("_".plus(_uiState.value.refProduct)).uppercase()
    }

    fun hideDialog(){
        _uiState.update {
            it.copy(showDialogError = false)
        }
    }
   private fun deleteAllFields(){
       _uiState.update {
           it.copy(kind = "", manufacturer = "", refProduct = "", pricePerUnit = "", stockActual = "")
       }
   }

    private fun checkFormatNumber(): Boolean {
        return try {
           _uiState.value.pricePerUnit.toDouble()
            _uiState.value.stockActual.toInt()
            true
        }catch (e:NumberFormatException){
            false
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRegisterNewWire(): RegisterNewProduct {
        return  RegisterNewProduct(id=getNewWireId(), refProduct = _uiState.value.refProduct.uppercase(),
            user=ActualUser.getActualUser().name!!, dateIn = actualDateTime(),
            pricePerUnit = _uiState.value.pricePerUnit.toDouble(), initialStock = _uiState.value.stockActual.toInt())
    }
    fun logOut(navigateToLogin:()->Unit){
        if(Connectivity.connectOk()==true){
            if(authService.logout()){
                ActualUser.resetActualUser()
                navigateToLogin()
            }
        }else{
            _uiState.update {
                it.copy(showDialogError = true)
            }
        }
    }

}

data class UiStateNewWire(val id:String="WIRE",
                            val kind:String="",
                          val manufacturer:String="",
                          val refProduct:String="",
                          val pricePerUnit:String="",
                          val stockActual:String="",
                          val enableButton:Boolean=false,
                          val showDialogError:Boolean=false,
                          val messageDialog:String="",
                          val user:String=""
                          )