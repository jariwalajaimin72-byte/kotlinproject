package com.example.kotlinproject.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.kotlinproject.R
import com.example.kotlinproject.activity.EditProfileActivity
import com.example.kotlinproject.activity.FavoriteActivity
import com.example.kotlinproject.activity.LoginActivity
import com.example.kotlinproject.activity.NotificationActivity
import com.example.kotlinproject.activity.ShippingAddressActivity
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileFragment : Fragment() {

    private lateinit var cardEditProfile: CardView
    private lateinit var cardNotification: CardView
    private lateinit var cardFavorited: CardView
    private lateinit var cardShippingAddress: CardView
    private lateinit var cardLogout: CardView
    private lateinit var txtUserName: TextView
    private lateinit var txtUserEmail: TextView
    private lateinit var profileImage: CircleImageView

    private val EDIT_PROFILE_REQUEST = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // 🔹 Bind views
        cardEditProfile = view.findViewById(R.id.card_edit_profile)
        cardNotification = view.findViewById(R.id.card_notification)
        cardFavorited = view.findViewById(R.id.card_favorited)
        cardShippingAddress = view.findViewById(R.id.card_shipping_address)
        cardLogout = view.findViewById(R.id.card_profile_logout)
        txtUserName = view.findViewById(R.id.user_name)
        txtUserEmail = view.findViewById(R.id.user_email)
        profileImage = view.findViewById(R.id.profile_image)

        // 🔹 Load saved data
        loadUserDetails()

        // 🔹 Click listeners
        cardEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST)
        }

        cardNotification.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }

        cardFavorited.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteActivity::class.java))
        }

        cardShippingAddress.setOnClickListener {
            startActivity(Intent(requireContext(), ShippingAddressActivity::class.java))
        }

        cardLogout.setOnClickListener {
            logoutUser()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // ✅ Reload data when returning to fragment
        loadUserDetails()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            loadUserDetails()
        }
    }

    private fun loadUserDetails() {
        val sp = requireActivity().getSharedPreferences("user_session", 0)
        val name = sp.getString("user_name", "Guest User")
        val email = sp.getString("user_email", "guest@example.com")
        val imageUri = sp.getString("user_image", null)

        txtUserName.text = name
        txtUserEmail.text = email

        if (!imageUri.isNullOrEmpty()) {
            profileImage.setImageURI(Uri.parse(imageUri))
        } else {
            profileImage.setImageResource(R.mipmap.logo_one)
        }
    }

    private fun logoutUser() {
        val sp = requireActivity().getSharedPreferences("user_session", 0)
        sp.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
