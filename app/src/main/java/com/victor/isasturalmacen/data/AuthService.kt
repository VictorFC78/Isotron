package com.victor.isotronalmacen.data


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.victor.isasturalmacen.data.ActualUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthService @Inject constructor(private val firebaseAuth : FirebaseAuth){

    suspend fun login (email:String, password:String): FirebaseUser? {

        return  firebaseAuth.signInWithEmailAndPassword(email,password).await().user

    }

    fun isUserLogged(): Boolean {
       return getCurrentUser() !=null
    }
     fun getCurrentUser() =firebaseAuth.currentUser

   fun logout() :Boolean{
        if(isUserLogged()){
            ActualUser.resetActualUser()
            firebaseAuth.signOut()
            return true
        }
    return false
    }

   suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser? {
       return firebaseAuth.createUserWithEmailAndPassword(email, password).await().user

    }

    suspend fun changePassword(newPassword:String){
        getCurrentUser()!!.updatePassword(newPassword).await()
    }
    fun deleteUserByEmail(): Boolean {
        return try {
            getCurrentUser()!!.delete()
            true
        }catch (e:Exception){
            false
        }
    }

}
