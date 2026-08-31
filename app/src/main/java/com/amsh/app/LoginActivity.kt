package com.amsh.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amsh.app.network.ApiClient
import com.amsh.app.network.ApiService
import com.amsh.app.network.LoginRequest
import com.amsh.app.network.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView

    private val api = ApiClient.retrofit.create(ApiService::class.java)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // ignore outcome for now
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)

        btnLogin.setOnClickListener {
            attemptLogin()
        }

        requestNotificationPermissionIfNeeded()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun attemptLogin() {
        tvError.visibility = View.GONE
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            tvError.text = getString(R.string.error_invalid_credentials)
            tvError.visibility = View.VISIBLE
            return
        }

        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false

        api.login(LoginRequest(email, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    saveToken(token)
                    // افتح MainActivity
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    tvError.text = getString(R.string.error_invalid_credentials)
                    tvError.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true
                tvError.text = getString(R.string.error_network)
                tvError.visibility = View.VISIBLE
            }
        })
    }

    private fun saveToken(token: String) {
        val prefs = getSharedPreferences("amsh_prefs", MODE_PRIVATE)
        prefs.edit().putString("auth_token", token).apply()
    }
}
