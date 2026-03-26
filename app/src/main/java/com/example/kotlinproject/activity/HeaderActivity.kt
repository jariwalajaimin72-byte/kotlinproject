package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.kotlinproject.R
import com.example.kotlinproject.ui.contact.ContactUsActivity
import com.google.android.material.navigation.NavigationView

class HeaderActivity: AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.user_navigation)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // ✅ Handle drawer menu item clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.user_privacy_menu -> {
                    startActivity(Intent(this, PrivacyPolicyActivity::class.java))
                }
                R.id.user_Terms_menu -> {
                    startActivity(Intent(this, TermsConditionsActivity::class.java))
                }
                R.id.user_contact_menu -> {
                    startActivity(Intent(this, ContactUsActivity::class.java))
                }
                R.id.user_review_menu -> {
                    startActivity(Intent(this, ReviewFeedbackActivity::class.java))
                    //Toast.makeText(this, "Clicked Review", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // ✅ Handle header clicks
        val headerView: View = navigationView.getHeaderView(0)

        val userName: TextView = headerView.findViewById(R.id.user_name)
        val userEmail: TextView = headerView.findViewById(R.id.user_email)

        userName.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        userEmail.setOnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
