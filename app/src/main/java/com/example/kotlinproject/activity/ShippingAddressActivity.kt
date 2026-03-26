package com.example.kotlinproject.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R

class ShippingAddressActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddressLine: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: EditText
    private lateinit var etZip: EditText
    private lateinit var btnSaveAddress: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping_address)

        // 🔹 Initialize Views
        etFullName = findViewById(R.id.etFullName)
        etPhone = findViewById(R.id.etPhone)
        etAddressLine = findViewById(R.id.etAddressLine)
        etCity = findViewById(R.id.etCity)
        etState = findViewById(R.id.etState)
        etZip = findViewById(R.id.etZip)
        btnSaveAddress = findViewById(R.id.btnSaveAddress)
        btnBack = findViewById(R.id.etback)

        // 🔙 Back Button
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 💾 Save Address
        btnSaveAddress.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddressLine.text.toString().trim()
            val city = etCity.text.toString().trim()
            val state = etState.text.toString().trim()
            val zip = etZip.text.toString().trim()

            // ✅ Validation
            when {
                fullName.isEmpty() -> showToast("Please enter your full name")
                phone.isEmpty() -> showToast("Please enter your phone number")
                phone.length != 10 -> showToast("Phone number must be 10 digits")
                !phone.all { it.isDigit() } -> showToast("Phone number must contain only digits")
                address.isEmpty() -> showToast("Please enter address line")
                city.isEmpty() -> showToast("Please enter city")
                state.isEmpty() -> showToast("Please enter state")
                zip.isEmpty() -> showToast("Please enter zip code")
                else -> {
                    // 🔹 Address saved successfully
                    showToast("Address saved successfully!")
                    finish() // go back after save
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
