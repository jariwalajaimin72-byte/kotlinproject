package com.example.kotlinproject.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class UpiPaymentActivity : AppCompatActivity() {

    private var totalAmount: Double = 0.0
    private var productName: String = ""
    private var productImage: String = ""
    private var quantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upi_payment)
        supportActionBar?.hide()

        val btnPayNow = findViewById<Button>(R.id.btnPayNow)

        // ✅ Receive product data
        totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown"
        productImage = intent.getStringExtra("PRODUCT_IMAGE") ?: ""
        quantity = intent.getIntExtra("QUANTITY", 1)

        btnPayNow.setOnClickListener {

            try {
                val uri = Uri.parse("upi://pay?pa=ariwala.jaimin72@okaxis&pn=MyShop&am=$totalAmount&cu=INR")
                val upiIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(upiIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "No UPI app found!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Save order
            saveOrderToFirebase()
        }
    }

    private fun saveOrderToFirebase() {

        val sp = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sp.getString("user_id", "") ?: ""
        val userName = sp.getString("user_name", "Guest") ?: "Guest"

        if (userId.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val orderRef = FirebaseDatabase.getInstance().getReference("Orders")
        val orderId = orderRef.push().key!!

        val currentDate = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        ).format(Date())

        val orderMap = mapOf(
            "orderId" to orderId,
            "userId" to userId,
            "userName" to userName,
            "productName" to productName,
            "productImage" to productImage,
            "quantity" to quantity,
            "totalAmount" to totalAmount,
            "paymentMethod" to "UPI Payment",
            "orderStatus" to "Pending",
            "date" to currentDate
        )

        orderRef.child(orderId).setValue(orderMap)
            .addOnSuccessListener {

                val successIntent = Intent(this, OrderSuccessActivity::class.java)
                successIntent.putExtra("TOTAL_AMOUNT", totalAmount)
                successIntent.putExtra("PAYMENT_METHOD", "UPI Payment")
                successIntent.putExtra("PRODUCT_NAME", productName)
                startActivity(successIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}