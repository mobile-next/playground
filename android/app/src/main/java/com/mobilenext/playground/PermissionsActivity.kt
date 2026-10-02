package com.mobilenext.playground

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog

class PermissionsActivity : AppCompatActivity() {

    private val permissionRequestCode = 100
    private val delayedAlertMillis = 2000L

    // permission -> status text view; filled in onCreate
    private val permissionStatusViews = mutableMapOf<String, TextView>()
    private lateinit var alertResultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindPermission(Manifest.permission.CAMERA, R.id.camera_permission_status, R.id.request_camera_permission_button)
        bindPermission(Manifest.permission.ACCESS_FINE_LOCATION, R.id.location_permission_status, R.id.request_location_permission_button)
        // ponytail: below API 33 notifications need no runtime permission, so the status just reads Granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bindPermission(Manifest.permission.POST_NOTIFICATIONS, R.id.notifications_permission_status, R.id.request_notifications_permission_button)
        } else {
            findViewById<TextView>(R.id.notifications_permission_status).text = "Granted"
        }

        alertResultText = findViewById(R.id.alert_result)
        alertResultText.text = "No alert shown"

        findViewById<Button>(R.id.show_simple_alert_button).setOnClickListener { showSimpleAlert() }
        findViewById<Button>(R.id.show_confirm_alert_button).setOnClickListener { showConfirmAlert() }
        findViewById<Button>(R.id.show_three_button_alert_button).setOnClickListener { showThreeButtonAlert() }
        findViewById<Button>(R.id.show_prompt_alert_button).setOnClickListener { showPromptAlert() }
        findViewById<Button>(R.id.show_action_sheet_button).setOnClickListener { showActionSheet() }
        findViewById<Button>(R.id.show_bottom_sheet_button).setOnClickListener { showBottomSheet() }
        findViewById<Button>(R.id.show_delayed_alert_button).setOnClickListener {
            alertResultText.text = "Waiting for alert"
            Handler(Looper.getMainLooper()).postDelayed({ showSimpleAlert() }, delayedAlertMillis)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            permissions.forEach { updatePermissionStatus(it) }
        }
    }

    private fun bindPermission(permission: String, statusViewId: Int, buttonId: Int) {
        permissionStatusViews[permission] = findViewById(statusViewId)
        updatePermissionStatus(permission)
        findViewById<Button>(buttonId).setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(permission), permissionRequestCode)
        }
    }

    private fun updatePermissionStatus(permission: String) {
        val granted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        permissionStatusViews[permission]?.text = if (granted) "Granted" else "Not Granted"
    }

    private fun showSimpleAlert() {
        AlertDialog.Builder(this)
            .setTitle("Simple Alert")
            .setMessage("This is a simple alert")
            .setPositiveButton("OK") { _, _ -> alertResultText.text = "OK" }
            .setCancelable(false)
            .show()
    }

    private fun showConfirmAlert() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Alert")
            .setMessage("Do you want to continue?")
            .setPositiveButton("OK") { _, _ -> alertResultText.text = "OK" }
            .setNegativeButton("Cancel") { _, _ -> alertResultText.text = "Cancel" }
            .setCancelable(false)
            .show()
    }

    private fun showThreeButtonAlert() {
        AlertDialog.Builder(this)
            .setTitle("Three Button Alert")
            .setMessage("Pick one of three options")
            .setPositiveButton("Yes") { _, _ -> alertResultText.text = "Yes" }
            .setNegativeButton("No") { _, _ -> alertResultText.text = "No" }
            .setNeutralButton("Later") { _, _ -> alertResultText.text = "Later" }
            .setCancelable(false)
            .show()
    }

    private val sheetColors = arrayOf("Red", "Green", "Blue")

    private fun showActionSheet() {
        AlertDialog.Builder(this)
            .setTitle("Choose a Color")
            .setItems(sheetColors) { _, which -> alertResultText.text = sheetColors[which] }
            .setNegativeButton("Cancel") { _, _ -> alertResultText.text = "Cancel" }
            .setCancelable(false)
            .show()
    }

    private fun showBottomSheet() {
        val sheet = BottomSheetDialog(this)
        val options = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        sheetColors.forEach { color ->
            options.addView(Button(this).apply {
                text = color
                setOnClickListener {
                    alertResultText.text = color
                    sheet.dismiss()
                }
            })
        }
        sheet.setContentView(options)
        sheet.setOnCancelListener { alertResultText.text = "Cancel" }
        sheet.show()
    }

    private fun showPromptAlert() {
        val input = EditText(this).apply { contentDescription = "prompt_alert_input" }
        AlertDialog.Builder(this)
            .setTitle("Prompt Alert")
            .setMessage("What is your name?")
            .setView(input)
            .setPositiveButton("OK") { _, _ -> alertResultText.text = "Hello, ${input.text}" }
            .setNegativeButton("Cancel") { _, _ -> alertResultText.text = "Cancel" }
            .setCancelable(false)
            .show()
    }
}
