package com.example.carevault.Articles

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.carevault.Adapters.NewsClicked
import com.example.carevault.Adapters.NewwsAdapter
import com.example.carevault.NewsService
import com.example.carevault.R
import com.example.carevault.news1
import com.example.carevault.news2
import retrofit2.Call
import retrofit2.Callback

class Health : AppCompatActivity(), NewsClicked {
    private lateinit var mAdapter: NewwsAdapter
    lateinit var recyclerview: RecyclerView
    lateinit var swipe: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)
        swipe=findViewById<SwipeRefreshLayout>(R.id.swipe)
        recyclerview=findViewById<RecyclerView>(R.id.recycleview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        getNews()

        swipe.setOnRefreshListener {
            getNews()
            swipe.isRefreshing = false
        }
    }
    private fun getNews() {
        val news = NewsService.newsInstance.getHeadlines("in", "health")
        news.enqueue(object : Callback<news1> {
            override fun onResponse(call: Call<news1>, response: retrofit2.Response<news1>) {
                val news = response.body()
                if (news != null) {
                    Log.d("news", news.toString())
                    val layoutManager = LinearLayoutManager(this@Health)
                    recyclerview.layoutManager = layoutManager
                    recyclerview.addItemDecoration(DividerItemDecoration(this@Health, layoutManager.orientation))
                    mAdapter = NewwsAdapter(this@Health, this@Health, news.articles)
                    recyclerview.adapter = mAdapter
                }
            }

            override fun onFailure(call: Call<news1>, t: Throwable) {
                Log.d("news", "error", t)
            }
        })
    }

    override fun onItemClicked(articles: news2) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(articles.url))
    }
}