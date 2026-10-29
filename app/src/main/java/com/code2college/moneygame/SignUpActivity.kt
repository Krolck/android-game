package com.code2college.moneygame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.code2college.moneygame.util.PasswordHasher
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.UUID

class SignUpActivity : ComponentActivity() {

    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvGoToLogin = findViewById<TextView>(R.id.tvGoToLogin)

        btnSignUp.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                tvErrorMessage.text = getString(R.string.error_empty_signup_fields)
                tvErrorMessage.visibility = View.VISIBLE
            } else if (password.length < 8) {
                tvErrorMessage.text = getString(R.string.error_password_too_short)
                tvErrorMessage.visibility = View.VISIBLE
            } else if (password != confirmPassword) {
                tvErrorMessage.text = getString(R.string.error_passwords_do_not_match)
                tvErrorMessage.visibility = View.VISIBLE
            } else {
                tvErrorMessage.visibility = View.GONE
                btnSignUp.isEnabled = false

                db.collection("Accounts").document(username).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            btnSignUp.isEnabled = true
                            tvErrorMessage.text = getString(R.string.error_username_taken)
                            tvErrorMessage.visibility = View.VISIBLE
                        } else {
                            val accountId = UUID.randomUUID().toString()
                            val salt = PasswordHasher.generateSalt()
                            val passwordHash = PasswordHasher.hashPassword(password, salt)
                            val initialUpgrades = GameManager.upgrades.associate { it.id to 0 }

                            val newAccount = mapOf(
                                "accountId" to accountId,
                                "username" to username,
                                "passwordHash" to passwordHash,
                                "salt" to salt,
                                "money" to 0,
                                "upgrades" to initialUpgrades
                            )

                            db.collection("Accounts").document(username)
                                .set(newAccount)
                                .addOnSuccessListener {
                                    btnSignUp.isEnabled = true
                                    GameManager.resetUserData(username, accountId)

                                    val intent = Intent(this, HomeActivity::class.java).apply {
                                        putExtra("EXTRA_USERNAME", username)
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    btnSignUp.isEnabled = true
                                    if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                                        tvErrorMessage.text = getString(R.string.error_permission_denied)
                                    } else {
                                        tvErrorMessage.text = getString(R.string.error_create_account_failed, e.localizedMessage ?: "")
                                    }
                                    tvErrorMessage.visibility = View.VISIBLE
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        btnSignUp.isEnabled = true
                        if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                            tvErrorMessage.text = getString(R.string.error_permission_denied)
                        } else {
                            tvErrorMessage.text = getString(R.string.error_signup_failed, e.localizedMessage ?: "")
                        }
                        tvErrorMessage.visibility = View.VISIBLE
                    }
            }
        }

        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
