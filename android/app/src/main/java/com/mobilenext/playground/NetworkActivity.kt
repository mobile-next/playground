package com.mobilenext.playground

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

// Sends HTTP requests through different stacks and shows the outcome in one text, so a
// test can tell which stacks a network tool sees. The result reads
// "<stack> <METHOD> <status> <body>" or "<stack> <METHOD> error: <message>", and the
// content description mirrors it so it shows up in the UI tree.
class NetworkActivity : AppCompatActivity() {
    companion object {
        const val GET_URL = "https://jsonplaceholder.typicode.com/todos/1"
        const val POST_URL = "https://jsonplaceholder.typicode.com/posts"
        const val POST_BODY = """{"title":"playground","body":"hello","userId":1}"""
        private const val MAX_BODY_CHARS = 300
    }

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.title = "Network"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        result = findViewById(R.id.network_result)
        showResult("idle")

        findViewById<Button>(R.id.btn_okhttp_get).setOnClickListener { okHttpGet() }
        findViewById<Button>(R.id.btn_okhttp_post).setOnClickListener { okHttpPost() }
        findViewById<Button>(R.id.btn_urlconnection_get).setOnClickListener { urlConnectionGet() }
    }

    private fun okHttpGet() {
        showResult("okhttp GET pending")
        val request = Request.Builder().url(GET_URL).build()
        PlaygroundApplication.httpClient.newCall(request).enqueue(resultCallback("okhttp GET"))
    }

    private fun okHttpPost() {
        showResult("okhttp POST pending")
        val body = POST_BODY.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder().url(POST_URL).post(body).build()
        PlaygroundApplication.httpClient.newCall(request).enqueue(resultCallback("okhttp POST"))
    }

    private fun urlConnectionGet() {
        showResult("urlconnection GET pending")
        thread {
            val label = "urlconnection GET"
            try {
                val connection = URL(GET_URL).openConnection() as HttpURLConnection
                connection.connectTimeout = 15_000
                connection.readTimeout = 15_000
                val status = connection.responseCode
                val stream = if (status >= 400) connection.errorStream else connection.inputStream
                val text = stream?.bufferedReader()?.use { it.readText() } ?: ""
                showResult("$label $status ${compact(text)}")
            } catch (e: Exception) {
                showResult("$label error: ${e.message}")
            }
        }
    }

    private fun resultCallback(label: String) = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            showResult("$label error: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val text = response.use { it.body?.string() ?: "" }
            showResult("$label ${response.code} ${compact(text)}")
        }
    }

    // One line, so the whole result fits in a single UI tree label.
    private fun compact(text: String): String {
        return text.replace(Regex("\\s+"), " ").trim().take(MAX_BODY_CHARS)
    }

    private fun showResult(text: String) {
        runOnUiThread {
            result.text = text
            result.contentDescription = text
        }
    }
}
