package com.dhinakaran.zohotest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhinakaran.zohotest.Adapters.NewsAdapter
import com.dhinakaran.zohotest.Models.NewsDataModel
import org.json.JSONArray
import org.json.JSONObject

class NewsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val newsdata = mutableListOf<NewsDataModel>()
    val adapters = NewsAdapter(newsdata)

    val limit = 1
    var page = 1;
    var isLoading = false

    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.news_rv)

        getData()
        recyclerView?.adapter = adapters
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView?.layoutManager = linearLayoutManager

//        TODO("Pagination")

//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) {
//                    val vicount = linearLayoutManager.childCount
//                    val pastVisibleItemCount =
//                        linearLayoutManager.findFirstCompletelyVisibleItemPosition()
//                    val total = adapters.itemCount
//                    if (!isLoading) {
//                        if ((vicount + pastVisibleItemCount) >= total) {
//                            page++
//                            getPage()
//                        }
//                    }
//                }
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })


        return view
    }

    //Pagination:
    fun getPage() {
        isLoading = true
        val start: Int = (page - 1) * limit
        val end: Int = (page) * limit
        isLoading = false
    }

    fun getData() {
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        val url = "https://inshorts.deta.dev/news?category=all"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            println(response)
            val jsonArray = response.optJSONArray("data")
            println(jsonArray)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val authorName = jsonObject.optString("author")
                val title = jsonObject.optString("title")
                val readMoreUrl = jsonObject.optString("readMoreUrl")
                val url = jsonObject.optString("url")
                val imageUrl = jsonObject.optString("imageUrl")
                val description =
                    "[" + jsonObject.optString("date") + "," + jsonObject.optString("time") + "] " + jsonObject.optString(
                        "content"
                    )
                println(authorName + " " + title + " " + readMoreUrl + " " + imageUrl)
                newsdata.add(i, NewsDataModel(title, imageUrl, description, readMoreUrl, url))
                adapters.notifyDataSetChanged()
            }

        }, { error ->
            Toast.makeText(
                requireContext(),
                "Fail to get response " + error.message,
                Toast.LENGTH_SHORT
            )
                .show()
            getData()
        })
        queue.add(request)
    }

}