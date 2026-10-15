package com.code2college.moneygame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class LoginActivity : ComponentActivity() {

    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoToSignUp = findViewById<TextView>(R.id.tvGoToSignUp)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                tvErrorMessage.text = getString(R.string.error_empty_login_fields)
                tvErrorMessage.visibility = View.VISIBLE
            } else {
                tvErrorMessage.visibility = View.GONE
                btnLogin.isEnabled = false

                db.collection("Accounts").document(username).get()
                    .addOnSuccessListener { document ->
                        btnLogin.isEnabled = true
                        if (!document.exists()) {
                            tvErrorMessage.text = getString(R.string.error_username_not_found)
                            tvErrorMessage.visibility = View.VISIBLE
                        } else {
                            val savedPassword = document.getString("password")
                            if (savedPassword != password) {
                                tvErrorMessage.text = getString(R.string.error_incorrect_password)
                                tvErrorMessage.visibility = View.VISIBLE
                            } else {
                                tvErrorMessage.visibility = View.GONE

                                val accountId = document.getString("accountId")
                                val savedMoney = (document.getLong("money") ?: 0L).toInt()
                                val upgradesRaw = document.get("upgrades") as? Map<*, *>
                                val upgradeLevelsMap = mutableMapOf<String, Int>()

                                upgradesRaw?.forEach { (key, value) ->
                                    val id = key.toString()
                                    val level = (value as? Long)?.toInt() ?: 0
                                    upgradeLevelsMap[id] = level
                                }

                                GameManager.loadUserData(username, accountId, savedMoney, upgradeLevelsMap)

                                val intent = Intent(this, HomeActivity::class.java).apply {
                                    putExtra("EXTRA_USERNAME", username)
                                }
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        btnLogin.isEnabled = true
                        if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                            tvErrorMessage.text = getString(R.string.error_permission_denied)
                        } else {
                            tvErrorMessage.text = getString(R.string.error_login_failed, e.localizedMessage ?: "")
                        }
                        tvErrorMessage.visibility = View.VISIBLE
                    }
            }
        }

        tvGoToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
}
