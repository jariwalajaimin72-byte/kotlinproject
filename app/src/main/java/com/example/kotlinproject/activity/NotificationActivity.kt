package com.example.kotlinproject.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val etBack = findViewById<ImageView>(R.id.etback)

        // ✅ Back button click works same as EditProfileActivity
        etBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
