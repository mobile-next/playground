package com.mobilenext.playground

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val webView = findViewById<WebView>(R.id.webview)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (request.url.scheme == "playground") {
                    startActivity(Intent(Intent.ACTION_VIEW, request.url))
                    return true
                }
                return false
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl("https://mobilewright.dev/samples/webview/?source=webview")
    }
}
