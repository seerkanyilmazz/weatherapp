package com.seerkanyilmazz.kotlin.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.io.File


class MainActivity : AppCompatActivity() {

    var CITY: String = "istanbul, tr"
    var API: String = "fbf23ac5af0a54e0446c7238aa74ec4d"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask() : AsyncTask<String, Void, String>(){
        override fun onPreExecute(){
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            var url = "https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API"
            try {
                response = URL(url).readText()
            }
            catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj        = JSONObject(result)
                val main           = jsonObj.getJSONObject("main")
                val sys            = jsonObj.getJSONObject("sys")
                val wind           = jsonObj.getJSONObject("wind")
                val weather        = jsonObj.getJSONArray("wind").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText  = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt * 1000))
                val temp           = main.getString("temp") + "C°"
                val tempMin        = "Min Temp: " + main.getString("tempMin") + "C°"
                val tempMax        = "Max Temp: " + main.getString("tempMax") + "C°"
                val pressure       = main.getString("pressure")
                val humidity       = main.getString("humidity")
                val sunrise        = sys.getLong("sunrise")
                val sunset         = sys.getLong("sunset")
                val windSpeed      = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.tempMin).text = tempMin
                findViewById<TextView>(R.id.tempMax).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
                findViewById<TextView>(R.id.errorText).visibility = View.GONE
            }
            catch (e : Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }
}