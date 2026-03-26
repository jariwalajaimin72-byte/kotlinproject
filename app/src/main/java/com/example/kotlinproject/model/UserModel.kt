package com.example.kotlinproject.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserModel(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var id: String=""
)
