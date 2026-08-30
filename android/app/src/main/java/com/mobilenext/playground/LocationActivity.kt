package com.mobilenext.playground

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar

class LocationActivity : AppCompatActivity(), LocationListener {

    private val locationPermissionRequestCode = 101
    private lateinit var coordinatesText: TextView
    private lateinit var mapView: WebView
    private var mapUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        coordinatesText = findViewById(R.id.coordinates)
        mapView = findViewById(R.id.map)
        mapView.settings.javaScriptEnabled = true

        findViewById<Button>(R.id.request_location_permission_button).setOnClickListener {
            requestLocation()
        }

        requestLocation()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            coordinatesText.text = "Permission not granted"
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
            return
        }

        // ponytail: plain LocationManager, no play-services-location dependency
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { onLocationChanged(it) }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            requestLocation()
        }
    }

    override fun onLocationChanged(location: Location) {
        coordinatesText.text = String.format("%.5f, %.5f", location.latitude, location.longitude)
        showOnMap(location.latitude, location.longitude)
    }

    // ponytail: openstreetmap embed needs no api key, google maps sdk would
    private fun showOnMap(latitude: Double, longitude: Double) {
        val delta = 0.01
        // ponytail: 4 decimals is ~11m, enough to keep gps jitter from reloading the map
        val bbox = String.format(
            "%.4f,%.4f,%.4f,%.4f",
            longitude - delta, latitude - delta, longitude + delta, latitude + delta
        )
        val marker = String.format("%.4f,%.4f", latitude, longitude)
        val url = "https://www.openstreetmap.org/export/embed.html?bbox=$bbox&marker=$marker"

        // ponytail: location updates tick every second, only reload when the map actually moved
        if (url == mapUrl) {
            return
        }

        mapUrl = url
        mapView.loadUrl(url)
    }
}
