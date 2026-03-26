package com.example.kotlinproject.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.kotlinproject.R
import com.example.kotlinproject.fragment.UserHomeFragment
import com.example.kotlinproject.fragment.UserProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.kotlinproject.fragment.UserCartFragment
import com.google.android.material.navigation.NavigationView
import com.example.kotlinproject.activity.PrivacyPolicyActivity
import com.example.kotlinproject.activity.TermsConditionsActivity
import com.example.kotlinproject.ui.contact.ContactUsActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bnvUser: BottomNavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        supportActionBar?.hide()

        drawerLayout = findViewById(R.id.drawerLayout)
        bnvUser = findViewById(R.id.bnvUser)
        navigationView = findViewById(R.id.user_navigation)

        // Drawer toggle
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Drawer Clicks
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

        // ✅ Bottom Navigation Clicks (FIXED)
        bnvUser.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.user_home_menu -> {
                    loadFragment(UserHomeFragment())
                }

                // 👉 Category ni jagyae ORDER ACTIVITY open thase
                R.id.user_orders_menu -> {
                    startActivity(Intent(this, UserOrderActivity::class.java))
                }

                R.id.user_cart_menu -> {
                    loadFragment(UserCartFragment())
                }

                R.id.user_profile_menu -> {
                    loadFragment(UserProfileFragment())
                }
            }
            true
        }

        // First load
        if (savedInstanceState == null) {
            loadFragment(UserHomeFragment())
            bnvUser.selectedItemId = R.id.user_home_menu
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.uframe, fragment)
            .commit()
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }
}