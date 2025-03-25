package com.victor.isotronalmacen.data


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.victor.isasturalmacen.data.ActualUser
import com.victor.isasturalmacen.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DataBaseService @Inject constructor(private val db:FirebaseFirestore) {
    private  val COLLECTION_USER = "users"
    private val HERRAMIENTAS ="HERRAMIENTAS"
    private val TOOLINPUTS_OUTPUTS ="TOOLINPUTS_OUTPUTS"
    private val DELETETOOLS ="DELETETOOLS"
    private val NEW_PRODUCTS = "NEWPRODUCTS"
    private val DELETE_PRODUCTS = "DELETEPRODUCTS"


    //recupera los datos del usuario logeado
    suspend fun recoverDataActualUser(email:String){
        val user =getDataUserByEmail(email)
        if (user != null) {
            ActualUser.updateActualUser(user)
        }
    }
    //recupera un usuario por su email
    suspend fun getDataUserByEmail(email: String): User? {
        return db.collection(COLLECTION_USER).document(email).get().await().toObject<User>()
    }
    //borra un usuario por su id
    suspend fun deleteUserByCollection(id:String): Boolean {
        return try {
            db.collection(COLLECTION_USER).document(id).delete().await()
            true
        }catch (e:Exception){
            false
        }

    }
    //añade los datos personales de un usuario recien registrado
    suspend fun updateFieldsUserWithEmail(id: String, user:User): Boolean {
        return db.collection(COLLECTION_USER).document(id).set(user).isSuccessful


    }
/*
    //recupera todas las herramientas mediante flow sin usar objecto en memoria
    fun getAllToolsFlow():Flow<List<Tool>>{
        return db.collection(HERRAMIENTAS)
            .snapshots().mapNotNull { qs->qs.toObjects(Tool::class.java) }

    }
    //recuperar una herramienta por id
    fun getTollByDocumentId(id:String): Flow<Tool> {
        return db.collection(HERRAMIENTAS)
            .document(id)
            .snapshots()
            .mapNotNull {qs->qs.toObject(Tool::class.java)  }
    }
    //recupera todas la herramientas
   suspend fun getAllTools(): List<Tool> {
           return try {
               db.collection(HERRAMIENTAS).get().await().toObjects<Tool>()
           } catch (e:Exception){
               listOf()
           }
    }
    // actualiza el estado de almacen de una herramienta
    @SuppressLint("SuspiciousIndentation")
    suspend fun updateDateTool(id: String?, value:Boolean?){
         val collectionId=
             db.collection(HERRAMIENTAS)
                 .document(id!!)
                 .update("inStore",value).await()
    }
    //añade registro a control salida entrada de herramientas
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addTooLoginAndToolLogout(input:Boolean,tool: Tool){
        val toolLog = when{
            input->createRegisterTool(tool,"ENTRADA")
            else->createRegisterTool(tool,"SALIDA")
        }
        db.collection(TOOLINPUTS_OUTPUTS).add(toolLog).await()
    }
    //crea un registro para añadirlo al control de entrada y salida de harramientas
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRegisterTool(tool: Tool, register:String): ToolLog {
        val data =DataToRegisterTool(tool).getData()
        return ToolLog(user = data.user, idTool = data.idTool,
            toolDescription = data.description, dateIn =data.date , inputOutput = register)
    }
    //borra una herramienta del base de datos
    suspend fun deleteTool(idTool:String){
           val document = db.collection(HERRAMIENTAS)
               .document(idTool).
               delete().await()
    }
    //añade un registrp de borrado de herramienta
   @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addDeleteToolRegister(tool: Tool){
        db.collection(DELETETOOLS).add(createDeleteToolLog(tool)).await()
    }
    //añade una herramienta a la base de datos
    suspend fun addNewTool(tool: Tool) {
        db.collection(HERRAMIENTAS).document(tool.id!!).set(tool).await()
    }
    //recupera todos los registros registro de entrada y salida de  material
    suspend fun getAllRegisterInputAndOutputsTools(): List<ToolLog> {
        return db.collection(TOOLINPUTS_OUTPUTS).get().await().toObjects<ToolLog>()
    }
    //recupera los registros de herramientas borradas
    suspend fun getAllToolDeleteRegister(): List<DeleteRegisterTool> {
        return db.collection(DELETETOOLS).get().await().toObjects<DeleteRegisterTool>()
    }
    //añade un registro de borrado de herramienta
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDeleteToolLog(tool: Tool): DeleteToolLog {
        val date = actualDateTime()
        return DeleteToolLog(user = ActualUser.getActualUser().name!!, idTool = tool.id!!, chargeDay = tool.chargeDay!!,
            dischargeDay = date, description = tool.description!!, pricePerDay = tool.pricePerDay!!)
    }


    //añade un registro de nuevo cable
    suspend fun addCreateRegisterNewProduct(registerNewAdd:RegisterNewProduct){
        db.collection(NEW_PRODUCTS).add(registerNewAdd).await()
    }

    //añade un registro al lista de borrado de productos
    suspend fun addRegisterDeleteProduct(registerDeleteProduct:RegisterDeleteProduct) {
        db.collection(DELETE_PRODUCTS).add(registerDeleteProduct).await()
    }


    //recupera un flow de lista de un producto por una coleccion
    fun getFlowListOfProductByCollection(kindOfCollection:String):Flow<List<Product>>{
        return db.collection(kindOfCollection)
            .snapshots()
            .mapNotNull {
                ns->ns.toObjects(Product::class.java)
            }
    }

    // añade un nuevo producto a la coleccion correspondiente
    suspend fun addNewProduct(kindOfCollection:String,product: Product){
        db.collection(kindOfCollection).document(product.id).set(product).await()
    }
    //actualiza el stock de un producto
    suspend fun updateStockProduct(kindOfCollection:String,id:String,amount:Int){
        db.collection(kindOfCollection).document(id).update("stockActual",amount).await()
    }
    //elimina un producto de la coleccion
    suspend fun deleteproductById(kindOfCollection:String,id:String){
        db.collection(kindOfCollection).document(id).delete().await()
    }
    //crea un registro de movimientos de stock de un producto
    suspend fun createRegisterStockProduct(kindOfCollection:String,registerStockProduct:RegisterStockProduct){
        db.collection(kindOfCollection).add(registerStockProduct).await()
    }
    //recupera una lista de productos segun una coleccion
    suspend fun getListOfProductByCollection(kindOfCollection:String):List<Product>{
        return db.collection(kindOfCollection).get().await().toObjects<Product>()
    }
    //recupera la lista de moivimientos de producto segun coleccion
    suspend fun getListRegisterStockProducts(kindOfCollection:String): List<RegisterStockProduct> {
        return db.collection(kindOfCollection).get().await().toObjects<RegisterStockProduct>()
    }*/
}