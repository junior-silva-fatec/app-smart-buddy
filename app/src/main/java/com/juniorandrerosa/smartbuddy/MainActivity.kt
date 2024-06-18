package com.juniorandrerosa.smartbuddy

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        textView.text = "Smart Buddy"

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            showLoginDialog()
        }
    }

    private fun showLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_login, null)
        val emailEditText = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = dialogView.findViewById<EditText>(R.id.editTextPassword)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Login")
            .setView(dialogView)
            .setPositiveButton("Login") { _, _ ->
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                doLogin(email, password)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private val client = OkHttpClient() // Create OkHttpClient instance
    private fun doLogin(email: String, password: String) {
        val jsonBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val jsonString = jsonBody.toString()

        val request = Request.Builder()
            .url("https://web-qx4yu7fnv0m1.up-us-nyc1-k8s-1.apps.run-on-seenode.com/login")
            .post(RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonString))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Falha na requisição: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    val statusCode = response.code
                    Toast.makeText(this@MainActivity, "Código de status: $statusCode", Toast.LENGTH_SHORT).show()
                    if (statusCode == 200) {
                        val intent = Intent(this@MainActivity, EventosActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    }
                }
            }
        })
    }
}
