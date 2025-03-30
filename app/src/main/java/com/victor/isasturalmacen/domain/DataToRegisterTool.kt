package com.victor.isasturalmacen.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.victor.isasturalmacen.auxs.actualDateTime
import com.victor.isasturalmacen.data.ActualUser

class DataToRegisterTool (private val tool: Tool) {
    private val user = ActualUser.getActualUser()

    @RequiresApi(Build.VERSION_CODES.O)
    private val date = actualDateTime()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(): DeleteRegisterTool {
        return DeleteRegisterTool(user = user.name!!,
            idTool = tool.id!!, date= date, description = tool.description!!)
    }



}
data class DeleteRegisterTool(val user:String="",
                              val idTool:String="",
                              val date:String="",
                              val description:String=""){
    override fun toString(): String {
        return "${date};${description};${idTool};${user}"
    }
}
