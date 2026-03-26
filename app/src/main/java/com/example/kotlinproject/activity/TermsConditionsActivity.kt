package com.example.kotlinproject.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject.R

class TermsConditionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_conditions)

        val termsText = """
            Terms & Conditions — Crochet Adorable Knots

Welcome to Crochet Adorable Knots. By accessing or using our mobile application, you agree to comply with and be bound by the following Terms and Conditions.

Please read them carefully.

1. Acceptance of Terms

By creating an account, browsing products, or placing an order through our app, you agree to these Terms & Conditions.
If you do not agree, please do not use the application.

2. Use of the App

You agree to:

Provide accurate and complete information

Use the app only for lawful purposes

Not misuse, hack, or attempt to damage the app

Not use the app for fraudulent activities

We reserve the right to suspend accounts that violate these terms.

3. Product Information

We strive to ensure that:

Product descriptions are accurate

Prices are correct

Images represent actual products

However, as crochet items are handmade:

Slight variations in color, size, or design may occur

Small differences are not considered defects

4. Orders & Payments

All orders are subject to confirmation.

We reserve the right to cancel any order due to pricing errors or stock issues.

Payments must be completed before order processing.

We do not store card or UPI details.

5. Shipping & Delivery

Delivery timelines may vary depending on location.

Delays caused by courier services are beyond our control.

Customers must provide accurate shipping details.

We are not responsible for delivery delays caused by incorrect addresses.

6. Returns & Refunds

Since crochet products are handmade:

Returns are accepted only for damaged or defective items.

Customers must report issues within 48 hours of delivery.

Refunds will be processed after verification.

Custom-made or personalized items are non-refundable.

7. Intellectual Property

All content in the app, including:

Logos

Product images

Designs

Text

belongs to Crochet Handmade Store and may not be copied or reused without permission.

8. Limitation of Liability

We are not liable for:

Indirect or incidental damages

Losses caused by misuse of the app

Service interruptions beyond our control

Use of the app is at your own risk.

9. Account Termination

We reserve the right to:

Suspend or terminate accounts

Remove users who violate policies

Cancel suspicious orders

10. Changes to Terms

We may update these Terms & Conditions at any time.
Changes will be posted inside the app with an updated date.

Continued use of the app means you accept the updated terms.

11. Contact Information

For any questions regarding these Terms & Conditions, contact:

Email: adorableknots@gmail.com
Contact No. : 7861969787
Developer: Crochet Adorable Knots Team
        """.trimIndent()

        val tvTermsContent: TextView = findViewById(R.id.tvTermsContent)
        tvTermsContent.text = termsText
    }
}
