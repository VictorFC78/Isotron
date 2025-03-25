package com.victor.isasturalmacen.data

import com.victor.isasturalmacen.domain.User



object ActualUser {
    private val user = User("","","")
    fun updateActualUser(newUser: User){
            user.name = newUser.name
            user.job = newUser.job
            user.credentials = newUser.credentials

    }
    fun getActualUser() = user
    fun  resetActualUser(){
        user.name = ""
        user.job = ""
        user.credentials = ""
    }

}