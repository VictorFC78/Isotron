package com.victor.isasturalmacen.domain

class RegisterNewProduct(val id:String="",
                         val refProduct:String="",
                         val user:String="",
                         val dateIn:String="",
                         val pricePerUnit:Double=0.0,
                         val initialStock:Int=0) {

    override fun toString(): String {
        return "${dateIn};${id};${initialStock};${pricePerUnit};${refProduct};${user}"
    }
}