package com.mobilenext.playground

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.appbar.MaterialToolbar

class PreferencesActivity : AppCompatActivity() {

    private val plainPrefsName = "playground_prefs"
    private val encryptedPrefsName = "playground_secure_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRevealButton()
        setupSaveButton()
        setupLoadButton()
        setupDeleteButton()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupRevealButton() {
        val passwordField = findViewById<EditText>(R.id.password_field)
        val revealButton = findViewById<Button>(R.id.reveal_button)
        var revealed = false

        revealButton.setOnClickListener {
            revealed = !revealed
            passwordField.inputType = if (revealed) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            revealButton.text = if (revealed) "Hide" else "Show"
            passwordField.setSelection(passwordField.text.length)
        }
    }

    private fun setupSaveButton() {
        findViewById<Button>(R.id.save_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.username_field).text.toString()
            val password = findViewById<EditText>(R.id.password_field).text.toString()

            getSharedPreferences(plainPrefsName, Context.MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .apply()

            encryptedPrefs().edit().putString("password", password).apply()

            setStatus("Saved")
        }
    }

    private fun setupLoadButton() {
        findViewById<Button>(R.id.load_button).setOnClickListener {
            val prefs = getSharedPreferences(plainPrefsName, Context.MODE_PRIVATE)
            val username = prefs.getString("username", null)
            val password = encryptedPrefs().getString("password", null)

            if (username == null && password == null) {
                setStatus("No preferences found")
                return@setOnClickListener
            }

            findViewById<EditText>(R.id.username_field).setText(username ?: "")
            findViewById<EditText>(R.id.password_field).setText(password ?: "")
            setStatus("Loaded")
        }
    }

    private fun setupDeleteButton() {
        findViewById<Button>(R.id.delete_button).setOnClickListener {
            getSharedPreferences(plainPrefsName, Context.MODE_PRIVATE)
                .edit()
                .remove("username")
                .apply()

            encryptedPrefs().edit().remove("password").apply()

            findViewById<EditText>(R.id.username_field).setText("")
            findViewById<EditText>(R.id.password_field).setText("")

            setStatus("Deleted")
        }
    }

    private fun encryptedPrefs() = EncryptedSharedPreferences.create(
        this,
        encryptedPrefsName,
        MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private fun setStatus(message: String) {
        findViewById<TextView>(R.id.status_message).text = message
    }
}
