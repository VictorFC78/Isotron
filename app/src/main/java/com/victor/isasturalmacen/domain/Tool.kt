package com.victor.isasturalmacen.domain



data class Tool(
    val id:String?="",
    val description:String?="",
    var pricePerDay:Double?=0.0,
    val chargeDay:String?="",
    val dischargeDay:String?="",
    var inStore:Boolean?=true,
    val imageUrl:String?=""){

    override fun toString(): String {
        return "${chargeDay};${description};${dischargeDay};${id};${imageUrl};${inStore};${pricePerDay};"

    }
}
