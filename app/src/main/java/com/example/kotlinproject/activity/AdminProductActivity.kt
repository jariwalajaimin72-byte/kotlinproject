package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_product)

        recyclerView = findViewById(R.id.etrecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fab = findViewById(R.id.fabAddProduct)
        fab.setOnClickListener {
            val intent = Intent(this@AdminProductActivity, AdminAddProductActivity::class.java)
            startActivity(intent)
        }
    }
}
