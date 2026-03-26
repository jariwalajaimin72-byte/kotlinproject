package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class CardPaymentActivity : AppCompatActivity() {

    private lateinit var edtCardNumber: EditText
    private lateinit var edtExpiry: EditText
    private lateinit var edtCvv: EditText
    private lateinit var btnPayNow: Button

    private var totalAmount: Double = 0.0
    private var productName: String = ""
    private var productImage: String = ""
    private var quantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardpayment)
        supportActionBar?.hide()

        edtCardNumber = findViewById(R.id.edtCardNumber)
        edtExpiry = findViewById(R.id.edtExpiry)
        edtCvv = findViewById(R.id.edtCVV)
        btnPayNow = findViewById(R.id.btnPayNow)

        // ✅ Receive product data
        totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown"
        productImage = intent.getStringExtra("PRODUCT_PIC") ?: ""
        quantity = intent.getIntExtra("QUANTITY", 1)

        btnPayNow.setOnClickListener {

            val cardNumber = edtCardNumber.text.toString().trim()
            val expiry = edtExpiry.text.toString().trim()
            val cvv = edtCvv.text.toString().trim()

            if (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Please fill all card details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveOrderToFirebase(cardNumber)
        }
    }

    private fun saveOrderToFirebase(cardNumber: String) {

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
            "paymentMethod" to "Card (****${cardNumber.takeLast(4)})",
            "orderStatus" to "Pending",
            "date" to currentDate
        )

        orderRef.child(orderId).setValue(orderMap)
            .addOnSuccessListener {

                val intent = Intent(this, OrderSuccessActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", totalAmount)
                intent.putExtra("PAYMENT_METHOD", "Card (****${cardNumber.takeLast(4)})")
                intent.putExtra("PRODUCT_NAME", productName)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}