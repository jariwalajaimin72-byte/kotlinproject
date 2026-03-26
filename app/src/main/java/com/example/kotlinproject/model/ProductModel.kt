package com.example.kotlinproject.model

import java.io.Serializable

data class ProductModel(
    val pid: String? = null,
    val pname: String? = null,
    val price: String? = null,
    var pic: String? = null,
    val desc: String? = null,
    val category: String? = null,
    var quantity: Int = 1   // 👈 default 1 rakhiye
) : Serializable
