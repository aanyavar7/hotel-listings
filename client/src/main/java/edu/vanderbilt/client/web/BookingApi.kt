package edu.vanderbilt.client.web

import edu.vanderbilt.client.model.Airport
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BookingApi {

    @GET("/airports")
    suspend fun getAirports(): List<Airport>

    companion object {
        var BASE_URL = "http://localhost:8081"
        fun create(): BookingApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(BookingApi::class.java)
        }
    }
}