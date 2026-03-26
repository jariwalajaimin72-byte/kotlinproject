package com.example.kotlinproject.model

data class OrderModel(
    var orderId: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var productId: String? = null,
    var productName: String? = null,
    var productImage: String? = null,
    var quantity: Int? = 1,
    var totalAmount: Double? = null,
    var paymentMethod: String? = null,
    var orderStatus: String? = "Pending",
    var date: String? = null
)
