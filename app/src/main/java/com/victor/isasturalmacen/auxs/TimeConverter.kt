package com.victor.isasturalmacen.auxs

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun actualDateTime(): String {
    val format =DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
    return LocalDateTime.now().format(format)
}
