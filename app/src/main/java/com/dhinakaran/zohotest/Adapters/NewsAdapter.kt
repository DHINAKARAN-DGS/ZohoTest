package com.dhinakaran.zohotest.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhinakaran.zohotest.Models.NewsDataModel
import com.dhinakaran.zohotest.NewsFragment
import com.dhinakaran.zohotest.R

class NewsAdapter(private val newsList: List<NewsDataModel>) :
    RecyclerView.Adapter<NewsAdapter.VIEWHOLDER>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.VIEWHOLDER {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_container, parent, false)
        return VIEWHOLDER(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.VIEWHOLDER, position: Int) {
        val title = newsList[position].title
        val description = newsList[position].description
        val thumbnail = newsList[position].thumbnail
        val readmore = newsList[position].readMore
        holder.setData(title, description, thumbnail, readmore)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class VIEWHOLDER(v: View) : RecyclerView.ViewHolder(v) {
        val TITLE: TextView = itemView.findViewById(R.id.title_news_container)
        val DES: TextView = itemView.findViewById(R.id.description_news_conatiner)

        //        val THUMB: TextView = itemView.findViewById(R.id.)
        val READMORE: TextView = itemView.findViewById(R.id.read_more_news_container)


        fun setData(title: String, description: String, thumbnail: String, readmore: String) {
            TITLE.setText(title)
            DES.setText(description)
        }

    }
}