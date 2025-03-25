package com.victor.isasturalmacen.domain

class Product(val id:String="",
              val kind:String = "",
              val pricePerUnit:Double = 0.0,
              var stockActual:Int = 0,
              val manufacturer:String = "",
              val refProduct:String = "") {

    override fun toString(): String {
        return "${id};${kind};${manufacturer};${pricePerUnit};${refProduct};${stockActual}"
    }
}