package com.victor.isasturalmacen.domain
class RegisterDeleteProduct (val id:String,
                             val stockActual:Int=0,
                             val manufacturer:String="",
                             val refProduct:String="",
                             val date:String="",
                             val user:String="") {

    override fun toString(): String {
        return "${date};${id};${manufacturer};${refProduct};${stockActual};${user}"
    }
}