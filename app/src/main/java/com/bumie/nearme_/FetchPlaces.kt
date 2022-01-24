package com.bumie.nearme_

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object FetchPlaces {

    suspend fun getToken(){

    }
    suspend fun getPlaces(client: OkHttpClient):ArrayList<Datum>{
        // Create an empty ArrayList that we can start adding colleges to
        val places: ArrayList<Datum> = ArrayList()
        coroutineScope{
            val fetchPlaces = async(Dispatchers.IO + SupervisorJob()) {
                val request: Request = Request.Builder()
                    .header("Authorization", "Bearer 2H33vZC3GRBBNJjMb2ccYBVpgxDu")
                    .url("https://test.api.amadeus.com/v1/reference-data/locations/pois?&latitude=41.397158&longitude=2.160873&radius=2")
                    .method("GET", null)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            throw IOException("Unexpected code $response")
                        } else {
                            val text = response.body!!.string()
                            Log.i("Main Activity: ", text)
                            //List<Datum> places = new ArrayList<>();
                            // If the JSON string is empty or null, then return early.

                            // Try to parse the JSON response string. If there's a problem with the way the JSON
                            // is formatted, a JSONException exception object will be thrown.
                            // Catch the exception so the app doesn't crash, and print the error message to the logs.
                            try {

                                // Create a JSONObject from the JSON response string
                                val baseJsonResponse = JSONObject(text)

                                // Extract the JSONArray associated with the key called "features",
                                // which represents a list of features (or colleges).
                                val collegeArray = baseJsonResponse.getJSONArray("data")

                                // For each college in the collegeArray, create an {@link College} object
                                for (i in 0 until collegeArray.length()) {

                                    // Get a single earthquake at position i within the list of college
                                    val currentCollege = collegeArray.getJSONObject(i)

                                    // Extract the value for the key called "title"
                                    val name = currentCollege.getString("name")

                                    val type = currentCollege.getString("type")

                                    val subType = currentCollege.getString("subType")

                                    val rank = currentCollege.getInt("rank")

                                    val resTags = currentCollege.getJSONArray("tags")
                                    val tags = arrayListOf<String>()
                                    for (x in 0 until resTags.length()){
                                       tags.add(resTags.getString(x))
                                    }

                                    // Extract the value for the key called "city"
                                    val category = currentCollege.getString("category")

                                    // Create a new {@link College} object with the city, latitude, longitude
                                    // from the JSON response.
                                    val college = Datum(name, category, type, subType, rank, tags)

                                    // Add the new {@link College} to the list of colleges.
                                    places.add(college)
                                }
                            } catch (e: JSONException) {
                                // If an error is thrown when executing any of the above statements in the "try" block,
                                // catch the exception here, so the app doesn't crash. Print a log message
                                // with the message from the exception.
                                Log.e("QueryUtils", "Problem parsing the college JSON results", e)
                            }
                        }
                        // do something wih the result
                        // Return the list of {@link Colleges}s
                    }
                })
            }
            try {
                fetchPlaces.await()
            }catch (e: Exception){
                Log.d("debugger: ", e.message!!)
            }
        }
        return places
    }

    suspend fun getPlaces_(client: OkHttpClient):ArrayList<Places>{
        // Create an empty ArrayList that we can start adding colleges to
        val places: ArrayList<Places> = ArrayList()
        coroutineScope{
            val fetchPlaces = async(Dispatchers.IO + SupervisorJob()) {
                val request: Request = Request.Builder()
                    .header("Authorization", "Bearer 2H33vZC3GRBBNJjMb2ccYBVpgxDu")
                    .url("https://test.api.amadeus.com/v1/reference-data/locations/pois?&latitude=41.397158&longitude=2.160873&radius=2")
                    .method("GET", null)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }
                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            throw IOException("Unexpected code $response")
                        } else {
                            val text = response.body!!.string()
                            Log.i("Main Activity: ", text)
                            //List<Datum> places = new ArrayList<>();
                            // If the JSON string is empty or null, then return early.

                            // Try to parse the JSON response string. If there's a problem with the way the JSON
                            // is formatted, a JSONException exception object will be thrown.
                            // Catch the exception so the app doesn't crash, and print the error message to the logs.
                            try {
                                val baseJsonResponse = JSONObject(text)
                                // Extract the JSONArray associated with the key called "features",
                                val collegeArray = baseJsonResponse.getJSONArray("data")

                                for (i in 0 until collegeArray.length()) {

                                    val currentCollege = collegeArray.getJSONObject(i)
                                    val name = currentCollege.getString("name")
                                    val geoCode = currentCollege.getJSONObject("geoCode")
                                    val latitude = geoCode.getDouble("latitude")
                                    val longitude = geoCode.getDouble("longitude")
                                    val category = currentCollege.getString("category")
                                    // Create a new {@link College} object with the city, latitude, longitude
                                    val college = Places(longitude, latitude, name, category)
                                    places.add(college)
                                }
                            } catch (e: JSONException) {
                                Log.e("QueryUtils", "Problem parsing the college JSON results", e)
                            }
                        }
                    }
                })
            }
            try {
                fetchPlaces.await()
            }catch (e: Exception){
                Log.d("debugger: ", e.message!!)
            }
        }
        return places
    }
}