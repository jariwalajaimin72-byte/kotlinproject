package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var eyeIcon: ImageView
    private lateinit var btnLogin: Button
    private lateinit var databaseRef: DatabaseReference

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        eyeIcon = findViewById(R.id.eyeIcon)
        btnLogin = findViewById(R.id.btnLogin)
        val tvRegisterNow = findViewById<TextView>(R.id.tvRegisterNow)

        databaseRef = FirebaseDatabase.getInstance().getReference("Users")

        // 👁️ PASSWORD HIDE / SHOW LOGIC
        eyeIcon.setOnClickListener {
            if (isPasswordVisible) {
                // Hide password
                passwordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                eyeIcon.setImageResource(R.drawable.ic_eye_closed)
                isPasswordVisible = false
            } else {
                // Show password
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                eyeIcon.setImageResource(R.drawable.ic_eye_open)
                isPasswordVisible = true
            }

            // Cursor hamesha end par rahe
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        tvRegisterNow.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Admin Login
            if (email.equals("admin", true) && password == "admin") {
                startActivity(Intent(this, AdminHomeActivity::class.java))
                finish()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {

        databaseRef.get().addOnSuccessListener { snapshot ->

            var isUserFound = false

            for (child in snapshot.children) {

                val dbEmail = child.child("email").value?.toString()?.trim() ?: ""
                val dbPassword = child.child("password").value?.toString()?.trim() ?: ""
                val dbFullName = child.child("fullname").value?.toString()?.trim() ?: ""

                if (dbEmail.equals(email, true) && dbPassword == password) {

                    isUserFound = true

                    // ✅ SAVE SESSION
                    val sp = getSharedPreferences("user_session", MODE_PRIVATE)
                    sp.edit().apply {
                        putString("user_id", child.key)
                        putString("user_name", dbFullName)
                        putString("user_email", dbEmail)
                        apply()
                    }

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    break
                }
            }

            if (!isUserFound) {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Database Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}