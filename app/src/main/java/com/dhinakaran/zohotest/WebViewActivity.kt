package com.dhinakaran.zohotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webViewReadMore)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = intent.getStringExtra("title").toString().subSequence(0,13)

        val url = intent.getStringExtra("url")

        webView.webViewClient = WebViewClient()

        if (url != null) {
            webView.loadUrl(url)
        }

        webView.settings.javaScriptEnabled = true

        webView.settings.setSupportZoom(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            if (webView.canGoBack())
                webView.goBack()
            else
                finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }

}