package com.mobilenext.playground

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

// Lists the action, data and extras the app was launched with, one row each, so a test
// can read them from the UI tree. Launch it directly (-n or the LAUNCH_INFO action), or
// open it from the main menu to see what MainActivity was launched with.
class LaunchInfoActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_LAUNCH_INTENT = "com.mobilenext.playground.LAUNCH_INTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_info)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.title = "Launch Info"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val launchIntent = forwardedIntent() ?: intent
        val rows = findViewById<LinearLayout>(R.id.rows)
        addRow(rows, "action", launchIntent.action ?: "(none)")
        addRow(rows, "data", launchIntent.dataString ?: "(none)")

        val extras = launchIntent.extras
        val keys = extras?.keySet()?.filter { it != EXTRA_LAUNCH_INTENT }?.sorted().orEmpty()
        if (keys.isEmpty()) {
            addRow(rows, "extras", "(none)")
        }
        for (key in keys) {
            addRow(rows, "extra.$key", describe(extras?.get(key)))
        }
    }

    private fun forwardedIntent(): Intent? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getParcelableExtra(EXTRA_LAUNCH_INTENT, Intent::class.java)
        }
        @Suppress("DEPRECATION")
        return intent.getParcelableExtra(EXTRA_LAUNCH_INTENT)
    }

    // Value and its type, so a test can tell --es 3 from --ei 3.
    private fun describe(value: Any?): String {
        if (value == null) {
            return "null"
        }
        val text = if (value is Array<*>) value.joinToString(",") else value.toString()
        return "$text (${value.javaClass.simpleName})"
    }

    // One text per row; the content description doubles as a stable id for tests.
    private fun addRow(rows: LinearLayout, key: String, value: String) {
        val row = TextView(this)
        row.text = "$key = $value"
        row.textSize = 16f
        row.setTextColor(0xFF000000.toInt())
        row.setPadding(0, 12, 0, 12)
        row.contentDescription = "$key = $value"
        rows.addView(row)
    }
}
