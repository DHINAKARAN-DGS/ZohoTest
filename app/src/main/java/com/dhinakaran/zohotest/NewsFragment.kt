package com.dhinakaran.zohotest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhinakaran.zohotest.Adapters.NewsAdapter
import com.dhinakaran.zohotest.Models.NewsDataModel

class NewsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val newsdata = mutableListOf<NewsDataModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_news, container, false)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.news_rv)
        newsdata.add(0, NewsDataModel("Title1","","",""))
        newsdata.add(1, NewsDataModel("Title2","","",""))
        newsdata.add(2, NewsDataModel("Title3","","",""))
        newsdata.add(3, NewsDataModel("Title4","","",""))
        newsdata.add(4, NewsDataModel("Title5","","",""))

        val adapters = NewsAdapter(newsdata)

        recyclerView?.adapter = adapters
        recyclerView?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        return view
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            NewsFragment().apply {

            }
    }
}