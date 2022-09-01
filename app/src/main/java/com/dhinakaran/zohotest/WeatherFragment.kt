package com.dhinakaran.zohotest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import org.json.JSONArray
import org.json.JSONObject


class WeatherFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_weather, container, false)

        getWeatherData()

        return view
    }

    fun getWeatherData(){
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        val url: String = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            println(response)
            val jsonOBj:JSONObject = response.getJSONObject("hourly")
            val timearrayJSON: JSONArray = jsonOBj.optJSONArray("time")
            val temparrayJSON: JSONArray = jsonOBj.optJSONArray("temperature_2m")
            println(timearrayJSON)
            println(temparrayJSON)


        }, { error ->
            Toast.makeText(requireContext(), "Fail to get response", Toast.LENGTH_SHORT)
                .show()
        })
        queue.add(request)
    }


}