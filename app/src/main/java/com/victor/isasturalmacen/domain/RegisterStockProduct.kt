package com.victor.isasturalmacen.domain

data class RegisterStockProduct(val user:String="",
                                val dateIn:String="",
                                val id:String="",
                                val refProduct:String="",
                                val outAmount:Int=0){

    override fun toString(): String {
        return "${dateIn};${id};${outAmount};${refProduct};${user};"
    }
}
