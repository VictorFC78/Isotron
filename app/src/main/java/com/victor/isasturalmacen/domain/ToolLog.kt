package com.victor.isasturalmacen.domain

data class ToolLog(
    val user:String? ="",
    val idTool:String?="",
    val toolDescription:String?="",
    val dateIn:String?="",
    val inputOutput:String=""){

    override fun toString(): String {
        return "${dateIn};${idTool};${inputOutput};${toolDescription};${user};"
    }
}
