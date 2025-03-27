package com.victor.isasturalmacen.domain

data class DeleteToolLog (val user:String = "",
                            val idTool:String ="",
                            val chargeDay:String ="",
                            val dischargeDay:String="",
                            val description:String="",
                            val pricePerDay:Double=0.0){
    override fun toString(): String {
        return "${chargeDay};${description};${dischargeDay};${idTool};${pricePerDay};${user};"
    }
}