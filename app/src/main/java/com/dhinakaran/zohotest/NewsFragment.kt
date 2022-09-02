package com.dhinakaran.zohotest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhinakaran.zohotest.Adapters.NewsAdapter
import com.dhinakaran.zohotest.Models.NewsDataModel
import kotlin.collections.ArrayList

class NewsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var newsdata = mutableListOf<NewsDataModel>()
    var searchNewsData = mutableListOf<NewsDataModel>()
    lateinit var newsAdapter: NewsAdapter
    lateinit var newsAdapterForSearch: NewsAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    var indexlist = mutableListOf<Int>()

    lateinit var linearLayoutManager: LinearLayoutManager


//    For Pagination
//    val limit = 1
//    var page = 1;
//    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)
        recyclerView = view.findViewById(R.id.news_rv)
        val searchBar = view.findViewById<SearchView>(R.id.search_bar)
        progressBar = view.findViewById(R.id.progressBarRV)
        searchBar.clearFocus()

        getData()

        searchNewsData = ArrayList<NewsDataModel>()
        searchNewsData.addAll(newsdata)

        newsAdapter = NewsAdapter(newsdata)
        newsAdapterForSearch = NewsAdapter(searchNewsData)

        recyclerView.adapter = newsAdapter
        newsAdapter.notifyDataSetChanged()
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    search(newText.uppercase())
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNewsData.clear()
                println(newsdata.size)
                for (i in newsdata) {
                    if (query != null) {
                        if (i.title.uppercase().contains(query)) {
                            searchNewsData.add(
                                NewsDataModel(
                                    i.title,
                                    i.thumbnail,
                                    i.description,
                                    i.readMore,
                                    i.contentUrl
                                )
                            )
                            newsAdapter.notifyDataSetChanged()
                        }
                    }
                }
                if (searchNewsData.size == 0) {
                    Toast.makeText(requireContext(), "No News Found", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })


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
//        TODO("Pagination")

        return view
    }

    @SuppressLint("Range")
    fun getData() {
        progressBar.visibility = View.VISIBLE
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
                newsAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            val db = DBHelper(requireContext(), null)

            val data = newsdata

            db.addJSON(data.toString())

            val cursor = db.getJSON()

            cursor!!.moveToFirst()

            var count = 0;

            while (cursor.moveToNext()) {
                count++
                val data: String = cursor.getString(cursor.getColumnIndex(DBHelper.JSON_COl))
                println(data)
            }
            Toast.makeText(requireContext(), "Data Stored in the SQL DB count "+count, Toast.LENGTH_SHORT).show()


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

    fun search(newText: String) {
        searchNewsData.clear()
        progressBar.visibility = View.VISIBLE
        indexlist.clear()
        println("a12 " + newsdata)
        newsdata.forEachIndexed { index, it ->
            if (newsdata[index].title.uppercase().contains(newText)) {
                indexlist.add(index)
            }
        }
        println("a" + indexlist.size)
        for (i in indexlist) {
            searchNewsData.add(
                NewsDataModel(
                    newsdata[i].title,
                    newsdata[i].thumbnail,
                    newsdata[i].description,
                    newsdata[i].readMore,
                    newsdata[i].contentUrl
                )
            )
            println(searchNewsData)
        }
        recyclerView.adapter = newsAdapterForSearch
        newsAdapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
        if (searchNewsData.size == 0) {
            Toast.makeText(requireContext(), "No News Found", Toast.LENGTH_SHORT).show()
        }
    }


//        TODO("Pagination")
//    fun getPage() {
//        isLoading = true
//        val start: Int = (page - 1) * limit
//        val end: Int = (page) * limit
//        isLoading = false
//    }
//        TODO("Pagination")

}

