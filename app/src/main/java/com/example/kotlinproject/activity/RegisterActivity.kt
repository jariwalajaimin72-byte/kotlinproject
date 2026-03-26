package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var eyeIconPass: ImageView
    private lateinit var eyeIconConPass: ImageView
    private lateinit var registerButton: Button
    private lateinit var databaseRef: DatabaseReference

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        databaseRef = FirebaseDatabase.getInstance().getReference("Users")

        nameInput = findViewById(R.id.etname)
        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etpassword)
        confirmPasswordInput = findViewById(R.id.etconpass)
        eyeIconPass = findViewById(R.id.eyeIconPass)
        eyeIconConPass = findViewById(R.id.eyeIconConPass)
        registerButton = findViewById(R.id.btnRegister)

        // 👁️ PASSWORD SHOW / HIDE
        eyeIconPass.setOnClickListener {
            if (isPasswordVisible) {
                passwordInput.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                eyeIconPass.setImageResource(R.drawable.ic_eye_closed)
                isPasswordVisible = false
            } else {
                passwordInput.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                eyeIconPass.setImageResource(R.drawable.ic_eye_open)
                isPasswordVisible = true
            }
            passwordInput.setSelection(passwordInput.text.length)
        }

        // 👁️ CONFIRM PASSWORD SHOW / HIDE
        eyeIconConPass.setOnClickListener {
            if (isConfirmPasswordVisible) {
                confirmPasswordInput.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                eyeIconConPass.setImageResource(R.drawable.ic_eye_closed)
                isConfirmPasswordVisible = false
            } else {
                confirmPasswordInput.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                eyeIconConPass.setImageResource(R.drawable.ic_eye_open)
                isConfirmPasswordVisible = true
            }
            confirmPasswordInput.setSelection(confirmPasswordInput.text.length)
        }

        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (isInputValid(name, email, password, confirmPassword)) {
                saveUserToFirebase(name, email, password)
            }
        }
    }

    private fun saveUserToFirebase(name: String, email: String, password: String) {
        val userId = databaseRef.push().key ?: return

        val userMap = mapOf(
            "id" to userId,
            "name" to name,
            "email" to email,
            "password" to password
        )

        databaseRef.get().addOnSuccessListener { snapshot ->
            var exists = false

            for (child in snapshot.children) {
                val dbEmail = child.child("email").value?.toString() ?: ""
                if (dbEmail.equals(email, ignoreCase = true)) {
                    exists = true
                    break
                }
            }

            if (exists) {
                Toast.makeText(
                    this,
                    "User already registered. Please login.",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                databaseRef.child(userId).setValue(userMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Registration Successful! Please Login.",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Registration Failed. Try Again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun isInputValid(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be 6+ characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}