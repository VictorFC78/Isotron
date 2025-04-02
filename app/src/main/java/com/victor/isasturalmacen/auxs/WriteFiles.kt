package com.victor.isasturalmacen.auxs

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate

object WriteFiles {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNewFileInDirectory(directory:String):File?{
      return   try {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val newDir = File(path,directory)
            if(!newDir.exists()) newDir.mkdirs()
            File(newDir, LocalDate.now().toString()+".csv")
        }catch (e:FileNotFoundException){
            null
        }
    }
    fun <T> writeDataInFile(file:File, listOfData:List<T>): Boolean {

        val buffer = FileOutputStream(file).bufferedWriter()
        return try {
            if(listOfData.isNotEmpty()){
                listOfData[0]!!::class
                    .java.declaredFields
                    .map { it.name }
                    .forEach {
                        buffer.write("${it};")
                    }
                buffer.newLine()
                listOfData.forEach {
                    buffer.write(it.toString())
                    buffer.newLine()
                }
                buffer.flush()
                buffer.close()
                true
            }else{
                false
            }

        }catch (e:IOException){
            false
        }
    }

}