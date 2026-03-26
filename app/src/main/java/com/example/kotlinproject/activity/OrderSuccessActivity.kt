package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class OrderSuccessActivity : AppCompatActivity() {

    private lateinit var btnContinue: Button
    private lateinit var txtMessage: TextView
    private lateinit var txtDetails: TextView

    private val dbRef = FirebaseDatabase.getInstance().getReference("Orders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_success)
        supportActionBar?.hide()

        initViews()
        handleOrder()
    }

    private fun initViews() {
        btnContinue = findViewById(R.id.btnContinueShopping)
        txtMessage = findViewById(R.id.txtMessage)
        txtDetails = findViewById(R.id.txtDetails)
    }

    private fun handleOrder() {

        // ✅ Get data from intent
        val totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        val paymentMethod = intent.getStringExtra("PAYMENT_METHOD") ?: "N/A"
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown"
        val productImage = intent.getStringExtra("PRODUCT_IMAGE") ?: ""

        // ✅ Get session
        val sp = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sp.getString("user_id", "")
        val userName = sp.getString("user_name", "Guest")

        txtMessage.text = "Order Placed Successfully!"
        txtDetails.text = """
            User: $userName
            Product: $productName
            Total: ₹%.2f
            Payment: $paymentMethod
        """.trimIndent().format(totalAmount)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val orderId = dbRef.push().key ?: return

        val currentTime = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        ).format(Date())

        // ✅ Save full order data
        val orderData = HashMap<String, Any>()
        orderData["orderId"] = orderId
        orderData["userId"] = userId
        orderData["userName"] = userName ?: "Guest"
        orderData["productName"] = productName
        orderData["productImage"] = productImage
        orderData["totalAmount"] = totalAmount
        orderData["paymentMethod"] = paymentMethod
        orderData["orderStatus"] = "Pending"
        orderData["date"] = currentTime

        dbRef.child(orderId).setValue(orderData)
            .addOnSuccessListener {
                Toast.makeText(this, "Order Saved Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Save Order", Toast.LENGTH_SHORT).show()
            }

        btnContinue.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}