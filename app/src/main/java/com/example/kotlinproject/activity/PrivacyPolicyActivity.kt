package com.example.kotlinproject.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        val privacyContent = """
          **Privacy Policy — Adorable Knots**

Welcome to **Crochet Adorable Knots**. Your privacy is very important to us. 
This Privacy Policy explains how we collect, use, and protect your information when you use our mobile application.

---

 1. Information We Collect

When you use our app, we may collect the following information:

**Personal Information**

* Name
* Email address
* Phone number
* Shipping address

This information is collected only when you:

* Create an account
* Place an order
* Save a shipping address
* Contact support

**Order Information**

* Products you purchase
* Order history
* Payment status (we do NOT store card details)

**Device Information (Automatically Collected)**

* Device type
* Android version
* App usage data (for improving app performance)

---

 2. How We Use Your Information

We use the collected information to:

* Process and deliver your orders
* Contact you regarding your purchase
* Provide customer support
* Improve app performance and user experience
* Prevent fraud or misuse of the app

We do **not sell or rent** your personal data to anyone.

---

 3. Payments

All payments are handled securely by third-party payment services (such as UPI, Razorpay, or other payment gateways).
We **do not store your debit/credit card information** on our servers.

---

 4. Data Storage & Security

We use secure technologies and Firebase database services to store your information safely.
We take reasonable measures to protect your personal data from unauthorized access, loss, or misuse.

However, no internet transmission is 100% secure, so we cannot guarantee absolute security.

---

 5. Cookies & Analytics

The app may use basic analytics tools (such as Firebase Analytics) to understand:

* Which products are viewed
* App performance
* Crash reports

This helps us improve the application experience.

---

 6. Third-Party Services

Our app may use trusted third-party services including:

* Firebase Authentication
* Firebase Realtime Database / Firestore
* Payment gateway providers

These services may collect limited technical information as required to operate their services.

---

 7. Children’s Privacy

Our app is not intended for children under the age of 13.
We do not knowingly collect personal information from children.

---

 8. Your Rights

You can:

* Update your profile information
* Request deletion of your account
* Contact us for data removal

To request account deletion, contact us at the email below.

---

 9. Changes to This Policy

We may update this Privacy Policy from time to time.
Changes will be posted inside the app with an updated date.

---

 10. Contact Us

If you have any questions about this Privacy Policy, please contact:

**Email:** adorableknots@gmail.com
**Contact No.:** 7861969787
**Developer:** Adorable Knots Team

---

By using this app, you agree to this Privacy Policy.

        """.trimIndent()

        val tvPrivacyContent: TextView = findViewById(R.id.tvPrivacyContent)
        tvPrivacyContent.text = privacyContent
    }
}
