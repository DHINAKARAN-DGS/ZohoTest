package com.dhinakaran.zohotest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WeatherFragment : Fragment() {

    val PERMISSION_ID = 42
    var lat = 0.0
    var long = 0.0
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    lateinit var location: TextView
    lateinit var avgTem: TextView
    lateinit var minTemp: TextView
    lateinit var maxTemp: TextView
    lateinit var sunrise: TextView
    lateinit var sunset: TextView
    lateinit var wind: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_weather, container, false)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        location = view.findViewById(R.id.current_location)
        avgTem = view.findViewById(R.id.current_weather_temp)
        minTemp = view.findViewById(R.id.min_temp)
        maxTemp = view.findViewById(R.id.max_temp)
        sunrise = view.findViewById(R.id.sunrise)
        sunset = view.findViewById(R.id.sunset)
        wind = view.findViewById(R.id.wind)

        getLastLocation()
        getWeatherData()


        return view
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        println("aa" + location.latitude)
                        println("aa" + location.longitude)
                        lat = location.latitude
                        long = location.longitude
                        getWeatherData()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            println("last" + locationResult.lastLocation.latitude)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }



    @SuppressLint("SetTextI18n")
    fun getWeatherData() {
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        val url: String =
            "https://api.open-meteo.com/v1/forecast?latitude="+lat+"&longitude="+long+"&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset,windspeed_10m_max&timezone=auto&past_days=1"


        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            println(response)

            val jsonOBj: JSONObject = response.getJSONObject("daily")
            val maxtemparrayJSON: JSONArray = jsonOBj.optJSONArray("temperature_2m_max")
            val mintemparrayJSON: JSONArray = jsonOBj.optJSONArray("temperature_2m_min")
            val sunrisearrayJSON: JSONArray = jsonOBj.optJSONArray("sunrise")
            val sunsetarrayJSON: JSONArray = jsonOBj.optJSONArray("sunset")
            val windspeedarrayJSON: JSONArray = jsonOBj.optJSONArray("windspeed_10m_max")
            println(jsonOBj)
            location.text = response.getString("timezone")
            sunrise.text = sunrisearrayJSON[2].toString().substring(11)
            sunset.text = sunsetarrayJSON[2].toString().substring(11)
            minTemp.text = "Min Temp "+mintemparrayJSON[2].toString()+"°C"
            maxTemp.text = "Max Temp "+maxtemparrayJSON[2].toString()+"°C"
            wind.text = windspeedarrayJSON[2].toString() //TODO(need to fetch realtime)
            avgTem.text = mintemparrayJSON[2].toString()+" °C" //TODO(need to get the current time temperature)

        }, {
            Toast.makeText(requireContext(), "Fail to get response", Toast.LENGTH_SHORT)
                .show()
        })
        queue.add(request)
    }


}