package com.example.kotlinproject.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.kotlinproject.R
import com.example.kotlinproject.fragment.AdminDashboardFragment
import com.example.kotlinproject.fragment.ReportFragment
import com.example.kotlinproject.fragment.fragment_admin_home
import com.example.kotlinproject.fragment.LogoutFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminHomeActivity : AppCompatActivity() {


    private lateinit var bnvAdminHome: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep edge-to-edge
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_home)

        bnvAdminHome = findViewById(R.id.bnvAdminHome)

        // 🔥 FIX: Apply system inset ONLY to BottomNavigationView
        ViewCompat.setOnApplyWindowInsetsListener(bnvAdminHome) { view, insets ->
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(0, 0, 0, navBarInsets.bottom)
            insets
        }

        // Default fragment
        addFragment(fragment_admin_home())

        // Bottom Navigation handling
        bnvAdminHome.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.admin_dash_menu -> {
                    replaceFragment(fragment_admin_home())
                    true
                }

                R.id.admin_report_menu -> {
                    replaceFragment(ReportFragment())
                    true
                }

                R.id.admin_logout_logout -> {
                    replaceFragment(LogoutFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, fragment)
            .commit()
    }


}
