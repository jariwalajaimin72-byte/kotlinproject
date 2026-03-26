package com.example.kotlinproject.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnBack: ImageView

    private val dbRef = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.hide()

        // 🔹 Initialize views
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnBack = findViewById(R.id.btnBack)

        val sp = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sp.getString("user_id", "") ?: ""

        // 🔹 Set existing user details
        etUsername.setText(sp.getString("user_name", ""))
        etEmail.setText(sp.getString("user_email", ""))
        etPassword.setText(sp.getString("user_password", ""))
        etPhone.setText(sp.getString("user_phone", ""))

        btnBack.setOnClickListener { finish() }

        btnUpdate.setOnClickListener {
            val name = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userId.isEmpty()) {
                Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔹 Prepare update map
            val updates = mapOf(
                "name" to name,
                "email" to email,
                "password" to password,
                "phone" to phone
            )

            // 🔹 Update in Firebase
            dbRef.child(userId).updateChildren(updates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 🔹 Update local session
                    sp.edit().apply {
                        putString("user_name", name)
                        putString("user_email", email)
                        putString("user_password", password)
                        putString("user_phone", phone)
                        apply()
                    }

                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                    // 🔹 Return success result to fragment
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
