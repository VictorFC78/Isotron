package com.victor.isasturalmacen.domain

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(
    var name:String? = "",
    var job:String? = "",
    var credentials:String? = "",
)
