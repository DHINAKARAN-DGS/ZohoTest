package com.dhinakaran.zohotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    private val menu by lazy { findViewById<ChipNavigationBar>(R.id.bottom_nav_bar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        menu.setOnItemSelectedListener {
            if (it == R.id.News) {
                showFragment(NewsFragment(), R.id.News)
            } else if (it == R.id.Weather) {
                showFragment(WeatherFragment(), R.id.Weather)
            }
        }

        menu.setItemSelected(R.id.News, true)

    }

    fun showFragment(fragment: Fragment, id: Int) {
        val fram = supportFragmentManager.beginTransaction()
        if (id == R.id.News) {
            fram.replace(R.id.fragment_layout, fragment)
            fram.commit()
        } else if (id == R.id.Weather) {
            fram.replace(R.id.fragment_layout, fragment)
            fram.commit()
        }
    }


}