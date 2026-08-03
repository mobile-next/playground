package com.mobilenext.playground

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.squareup.seismic.ShakeDetector

class MainActivity : AppCompatActivity(), ShakeDetector.Listener {
    private lateinit var sensorManager: SensorManager
    private lateinit var shakeDetector: ShakeDetector
    private var shakeAlert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector(this)

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

        findViewById<LinearLayout>(R.id.btn_continuous_animation).setOnClickListener {
            startActivity(Intent(this, ContinuousAnimationActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_permissions).setOnClickListener {
            startActivity(Intent(this, PermissionsActivity::class.java))
        }


    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        shakeDetector.stop()
        super.onPause()
    }

    override fun hearShake() {
        if (shakeAlert?.isShowing == true) return

        shakeAlert = AlertDialog.Builder(this)
            .setMessage("Shake gesture detected. Now I'm all dizzy!")
            .setPositiveButton("OK", null)
            .setOnDismissListener { shakeAlert = null }
            .show()
    }
}
