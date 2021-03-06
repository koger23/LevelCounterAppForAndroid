package com.kogero.levelcounter.activities

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kogero.levelcounter.*
import com.kogero.levelcounter.adapters.FriendsAdapter
import com.kogero.levelcounter.api.ApiClient
import com.kogero.levelcounter.listeners.RecyclerViewTouchListener
import com.kogero.levelcounter.models.RecyclerViewClickListener
import com.kogero.levelcounter.models.Statistics
import com.kogero.levelcounter.models.UserListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriendsActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    lateinit var pendingRequestsIcon: ImageButton

    private val friendList: ArrayList<UserListViewModel> = ArrayList()
    var adapter = FriendsAdapter(this, friendList)

    override fun onResume() {
        super.onResume()
        getFriends()
        getPendingRequests(this@FriendsActivity)
        adapter.notifyDataSetChanged()
    }

    override fun onRestart() {
        super.onRestart()
        getFriends()
        getPendingRequests(this@FriendsActivity)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }

        getFriends()

        val recyclerView = findViewById<RecyclerView>(R.id.rv_friend_list)
        recyclerView.layoutManager = LinearLayoutManager(this@FriendsActivity)
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerViewTouchListener(
                applicationContext,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val user = friendList[position]
                        getStatisticsById(this@FriendsActivity, user)
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }
                })
        )
        pendingRequestsIcon = findViewById(R.id.imgBtnPending)
        getPendingRequests(this@FriendsActivity)
    }

    private fun getFriends() {
        val call: Call<List<UserListViewModel>> = ApiClient.getClient.getFriends()
        call.enqueue(object : Callback<List<UserListViewModel>> {
            override fun onResponse(
                call: Call<List<UserListViewModel>>,
                response: Response<List<UserListViewModel>>
            ) {
                val userData: List<UserListViewModel>? = response.body()
                if (response.code() == 200) {
                    if (userData != null) {
                        friendList.clear()
                        for (udata in userData) {
                            friendList.add(udata)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(this@FriendsActivity, "Login expired.", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@FriendsActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<UserListViewModel>>, t: Throwable) {
                Toast.makeText(
                    this@FriendsActivity,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun getPendingRequests(
        context: Context
    ){
        val call: Call<List<UserListViewModel>> =
            ApiClient.getClient.getPendingRequests()
        call.enqueue(object : Callback<List<UserListViewModel>> {
            override fun onResponse(
                call: Call<List<UserListViewModel>>,
                response: Response<List<UserListViewModel>>
            ) {
                val pendingRequests: List<UserListViewModel>? = response.body()
                if (response.code() == 200) {
                    if (pendingRequests != null && pendingRequests.isNotEmpty()) {
                        pendingRequestsIcon.visibility = View.VISIBLE
                        pendingRequestsIcon.setOnClickListener {
                            val intent = Intent(this@FriendsActivity, PendingRequestActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        pendingRequestsIcon.visibility = View.INVISIBLE
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                    ApiClient.resetToken()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<UserListViewModel>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun getStatisticsById(
        context: Context,
        userShortResponse: UserListViewModel
    ) {
        val call: Call<Statistics> =
            ApiClient.getClient.getStatisticsById(userShortResponse.statisticsId)
        call.enqueue(object : Callback<Statistics> {
            override fun onResponse(
                call: Call<Statistics>,
                response: Response<Statistics>
            ) {
                val statistics: Statistics? = response.body()
                if (response.code() == 200) {
                    if (statistics != null) {
                        val intent = Intent(context, StatisticsActivity::class.java)
                        intent.putExtra("STATISTICS", statistics)
                        intent.putExtra("ISPENDING", userShortResponse.isPending)
                        intent.putExtra("ISBLOCKED", userShortResponse.isBlocked)
                        intent.putExtra("ISFRIEND", userShortResponse.isFriend)
                        intent.putExtra("USERNAME", userShortResponse.userName)
                        startActivity(intent)
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Login expired.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Statistics>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Could not connect to the server",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    companion object {
        fun newInstance(): FriendsActivity =
            FriendsActivity()
    }
}