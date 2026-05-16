package com.mobilenext.playground

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Glide.with(this)
            .load("https://mobilewright.dev/mobilewright-logo.png")
            .into(findViewById<ImageView>(R.id.logo))

        findViewById<LinearLayout>(R.id.btn_basic_ui).setOnClickListener {
            startActivity(Intent(this, BasicUIActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_web_view).setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_preferences).setOnClickListener {
            startActivity(Intent(this, PreferencesActivity::class.java))
        }
    }
}
