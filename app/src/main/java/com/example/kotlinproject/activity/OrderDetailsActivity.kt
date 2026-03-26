package com.example.kotlinproject.activity

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.model.OrderModel
import com.google.firebase.database.*

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var imgProduct: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvQuantity: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvPayment: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvDate: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var database: DatabaseReference
    private var orderId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        // Init Views
        imgProduct = findViewById(R.id.imgProduct)
        tvProductName = findViewById(R.id.tvProductName)
        tvQuantity = findViewById(R.id.tvQuantity)
        tvTotal = findViewById(R.id.tvTotal)
        tvPayment = findViewById(R.id.tvPayment)
        tvStatus = findViewById(R.id.tvStatus)
        tvDate = findViewById(R.id.tvDate)
        progressBar = findViewById(R.id.progressBar)

        orderId = intent.getStringExtra("orderId")

        if (orderId.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid Order", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        database = FirebaseDatabase.getInstance()
            .getReference("Orders")
            .child(orderId!!)

        loadOrderDetails()
    }

    // ✅ Load Order Data
    private fun loadOrderDetails() {

        progressBar.visibility = View.VISIBLE

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                progressBar.visibility = View.GONE

                if (snapshot.exists()) {

                    val order = snapshot.getValue(OrderModel::class.java)

                    if (order != null) {

                        tvProductName.text = order.productName ?: "N/A"
                        tvQuantity.text = "Quantity: ${order.quantity ?: 0}"
                        tvTotal.text = "Total: ₹${order.totalAmount ?: 0.0}"
                        tvPayment.text = "Payment: ${order.paymentMethod ?: "N/A"}"
                        tvStatus.text = "Status: ${order.orderStatus ?: "Pending"}"
                        tvDate.text = "Date: ${order.date ?: "N/A"}"

                        // 🎨 Status Color
                        when (order.orderStatus) {
                            "Pending" -> tvStatus.setTextColor(Color.parseColor("#FFA500"))
                            "Shipped" -> tvStatus.setTextColor(Color.BLUE)
                            "Delivered" -> tvStatus.setTextColor(Color.parseColor("#008000"))
                            "Cancelled" -> tvStatus.setTextColor(Color.RED)
                            else -> tvStatus.setTextColor(Color.BLACK)
                        }

                        // ✅ Image Load (Base64)
                        loadProductImage(imgProduct, order.productImage)

                    } else {
                        Toast.makeText(this@OrderDetailsActivity, "Order data error", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@OrderDetailsActivity, "Order not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@OrderDetailsActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // ✅ Base64 Image Loader
    private fun loadProductImage(imageView: ImageView, base64Image: String?) {
        if (!base64Image.isNullOrEmpty()) {
            try {
                val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageResource(R.mipmap.logo_one)
                }

            } catch (e: Exception) {
                imageView.setImageResource(R.mipmap.logo_one)
            }
        } else {
            imageView.setImageResource(R.mipmap.logo_one)
        }
    }
}