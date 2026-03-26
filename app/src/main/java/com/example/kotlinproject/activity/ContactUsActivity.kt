package com.example.kotlinproject.ui.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R
import android.widget.ImageView
import android.widget.TextView

class ContactUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us) // your XML file name

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val txtPhone = findViewById<TextView>(R.id.txtPhone)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtInstagram = findViewById<TextView>(R.id.txtInstagram)

        // Back button
        btnBack.setOnClickListener { finish() }

        // Phone click
        txtPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${txtPhone.text}")
            startActivity(intent)
        }

        // Email click
        txtEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:${txtEmail.text}")
            startActivity(intent)
        }

        // Instagram click
        txtInstagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://instagram.com/${txtInstagram.text}")
            startActivity(intent)
        }
    }
}
