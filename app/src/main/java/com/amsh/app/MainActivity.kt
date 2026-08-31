package com.amsh.app

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amsh.app.network.ApiClient
import com.amsh.app.network.ApiService
import com.amsh.app.network.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private val api = ApiClient.retrofit.create(ApiService::class.java)
    private val adapter = ItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            loadItems()
        }

        loadItems()
    }

    private fun loadItems() {
        tvError.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        api.getItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                if (response.isSuccessful && response.body() != null) {
                    adapter.submitList(response.body()!!)
                } else {
                    tvError.visibility = View.VISIBLE
                    tvError.text = getString(R.string.error_network)
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                tvError.visibility = View.VISIBLE
                tvError.text = getString(R.string.error_network)
            }
        })
    }
}
