package com.example.cs4530_mobileapp

import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.Throws

object NetworkUtils {
    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat="
    private const val LON = "&lon="
    private const val APPIDQUERY = "&appid="
    private const val app_id = "306cbb60c07c3edafb9446a16ab9f81c"
    @JvmStatic
    fun buildURLFromString(location: String): URL? {
        var myURL: URL? = null
        try {
            val data = location.split(",")
            myURL = URL(BASE_URL + data[0] + LON + data[1] + APPIDQUERY + app_id)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return myURL
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getDataFromURL(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        return try {
            val inputStream = urlConnection.inputStream

            //The scanner trick: search for the next "beginning" of the input stream
            //No need to user BufferedReader
            val scanner = Scanner(inputStream)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}