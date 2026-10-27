package com.code2college.moneygame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class LeaderboardActivity : ComponentActivity() {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private lateinit var llLeaderboardContainer: LinearLayout
    private var snapshotListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        llLeaderboardContainer = findViewById(R.id.llLeaderboardContainer)

        val btnBackToDashboard = findViewById<Button>(R.id.btnBackToDashboard)
        btnBackToDashboard.setOnClickListener {
            finish()
        }

        startRealtimeLeaderboardListener()
    }

    private fun startRealtimeLeaderboardListener() {
        snapshotListener = db.collection("Accounts")
            .orderBy("money", Query.Direction.DESCENDING)
            .limit(100)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("LeaderboardActivity", "Error listening to leaderboard: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    llLeaderboardContainer.removeAllViews()
                    val inflater = LayoutInflater.from(this)

                    var rank = 1
                    for (doc in snapshot.documents) {
                        val username = doc.getString("username") ?: doc.id
                        val money = (doc.getLong("money") ?: 0L).toInt()

                        val itemView = inflater.inflate(R.layout.item_leaderboard, llLeaderboardContainer, false)
                        val tvRank = itemView.findViewById<TextView>(R.id.tvRank)
                        val tvPlayerUsername = itemView.findViewById<TextView>(R.id.tvPlayerUsername)
                        val tvPlayerMoney = itemView.findViewById<TextView>(R.id.tvPlayerMoney)

                        val rankDisplay = when (rank) {
                            1 -> "🥇 #1"
                            2 -> "🥈 #2"
                            3 -> "🥉 #3"
                            else -> getString(R.string.rank_format, rank)
                        }

                        tvRank.text = rankDisplay
                        tvPlayerUsername.text = username
                        tvPlayerMoney.text = getString(R.string.money_format, money)

                        // Highlight current user
                        if (username == GameManager.currentUsername) {
                            itemView.setBackgroundColor(0xFFE8EAF6.toInt()) // Highlight background
                        }

                        llLeaderboardContainer.addView(itemView)
                        rank++
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        snapshotListener?.remove()
    }
}
