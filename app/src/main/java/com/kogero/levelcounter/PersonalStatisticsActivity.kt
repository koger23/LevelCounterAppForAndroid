package com.kogero.levelcounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kogero.levelcounter.model.Statistics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PersonalStatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actitvity_playerstat)

        val userName = "YOU"
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        tvUserName.text = userName

        val btnImage = findViewById<ImageView>(R.id.btnImgState)
        btnImage.visibility = View.INVISIBLE

        getPersonalStats(this@PersonalStatisticsActivity)
    }

    companion object {
        fun newInstance(): FriendsActivity = FriendsActivity()
    }

    private fun getPersonalStats(
        context: Context
    ) {
        val call: Call<Statistics> =
            ApiClient.getClient.getPersonalStats()
        call.enqueue(object : Callback<Statistics> {

            override fun onResponse(call: Call<Statistics>, response: Response<Statistics>) {
                if (response.code() == 200) {
                    val statistics = response.body()
                    if (statistics != null) {
                        setView(statistics)
                    }
                } else if (response!!.code() == 401) {
                    Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Statistics>, t: Throwable) {
                Toast.makeText(
                    this@PersonalStatisticsActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun setView(statistics: Statistics) {
        val tvGamesPlayed = findViewById<TextView>(R.id.tvGamesPlayed)
        val tvRoundsPlayed = findViewById<TextView>(R.id.tvRoundsPlayed)
        val tvWins = findViewById<TextView>(R.id.tvWins)
        val tvTimePlayed = findViewById<TextView>(R.id.tvTimePlayed)
        tvGamesPlayed.text = statistics.gamesPlayed.toString()
        tvRoundsPlayed.text = statistics.roundsPlayed.toString()
        tvWins.text = statistics.wins.toString()
        tvTimePlayed.text = statistics.playTime.toString()
    }
}