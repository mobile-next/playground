package com.mobilenext.playground

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class LoginSuccessfulActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_successful)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.title = "Login Successful"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = intent.data?.getQueryParameter("name") ?: "Guest"
        findViewById<TextView>(R.id.message).text = "You have successfully logged in to the native app, $name!"
    }
}
