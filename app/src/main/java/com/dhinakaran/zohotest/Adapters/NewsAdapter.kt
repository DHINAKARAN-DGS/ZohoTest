package com.dhinakaran.zohotest.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dhinakaran.zohotest.Models.NewsDataModel
import com.dhinakaran.zohotest.R
import com.dhinakaran.zohotest.WebViewActivity

class NewsAdapter(private val newsList: List<NewsDataModel>) :
    RecyclerView.Adapter<NewsAdapter.VIEWHOLDER>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VIEWHOLDER {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_container, parent, false)
        return VIEWHOLDER(view)
    }

    override fun onBindViewHolder(holder: VIEWHOLDER, position: Int) {
        val title = newsList[position].title
        val description = newsList[position].description
        val thumbnail = newsList[position].thumbnail
        val readmore = newsList[position].readMore
        val contentUrl = newsList[position].contentUrl
        holder.setData(title, description, thumbnail, readmore, contentUrl)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class VIEWHOLDER(v: View) : RecyclerView.ViewHolder(v) {
        val TITLE: TextView = itemView.findViewById(R.id.title_news_container)
        val DES: TextView = itemView.findViewById(R.id.description_news_conatiner)
        val THUMB: ImageView = itemView.findViewById(R.id.thumb_image_news_container)
        val READMORE: TextView = itemView.findViewById(R.id.read_more_news_container)


        fun setData(
            title: String,
            description: String,
            thumbnail: String,
            readmore: String,
            contentUrl: String
        ) {
            TITLE.setText(title)
            DES.setText(description)
            Glide.with(itemView.context).load(thumbnail).into(THUMB)
            READMORE.setOnClickListener {
                val intent = Intent(itemView.context, WebViewActivity::class.java)
                intent.putExtra("url", readmore)
                intent.putExtra("title", title)
                itemView.context.startActivity(intent)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, WebViewActivity::class.java)
                intent.putExtra("url", contentUrl)
                intent.putExtra("title", title)
                itemView.context.startActivity(intent)
            }
        }

    }
}