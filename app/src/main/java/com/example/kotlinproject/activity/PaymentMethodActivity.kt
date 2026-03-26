package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var btnPayNow: Button
    private lateinit var productImageView: ImageView
    private lateinit var databaseRef: DatabaseReference

    private var totalAmount: Double = 0.0
    private var userName: String = ""
    private var productName: String = ""
    private var productPic: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        supportActionBar?.hide()

        radioGroup = findViewById(R.id.radioGroupPayment)
        btnPayNow = findViewById(R.id.btnPayNow)

        val productId = intent.getStringExtra("PRODUCT_ID")
        if (productId != null) {
            fetchProductImage(productId)
        }

        databaseRef = FirebaseDatabase.getInstance().getReference("cart")



        // 🔹 Get Total Amount & Product Name from Intent
        totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown Product"

        // 🔹 Get user details from SharedPreferences (saved during login)
        val sp = getSharedPreferences("user_session", MODE_PRIVATE)
        userName = sp.getString("user_name", "Guest") ?: "Guest"

        btnPayNow.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (selectedId) {
                R.id.radioCOD -> goToSuccess("Cash on Delivery")
                R.id.radioUPI -> goToUpi()
                R.id.radioCard -> goToCard()
            }
        }
    }

    private fun goToCard() {
        val intent = Intent(this, CardPaymentActivity::class.java)
        intent.putExtra("TOTAL_AMOUNT", totalAmount)
        intent.putExtra("USER_NAME", userName)
        intent.putExtra("PRODUCT_NAME", productName)
//        intent.putExtra("PRODUCT_PIC", productPic)
        startActivity(intent)
    }

    private fun goToUpi() {
        val intent = Intent(this, UpiPaymentActivity::class.java)
        intent.putExtra("TOTAL_AMOUNT", totalAmount)
        intent.putExtra("USER_NAME", userName)
        intent.putExtra("PRODUCT_NAME", productName)
        startActivity(intent)
    }

    private fun goToSuccess(method: String) {
        val intent = Intent(this, OrderSuccessActivity::class.java)
        intent.putExtra("TOTAL_AMOUNT", totalAmount)
        intent.putExtra("PAYMENT_METHOD", method)
        intent.putExtra("USER_NAME", userName)
        intent.putExtra("PRODUCT_NAME", productName)
        startActivity(intent)
        finish()
    }
    private fun fetchProductImage(productId: String) {

        databaseRef.child(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(ProductModel::class.java)
                    productPic = product?.pic.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PaymentMethodActivity,
                        "Failed to load image",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }
}
