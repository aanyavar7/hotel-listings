package edu.vanderbilt.client.features.airport

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import edu.vanderbilt.client.features.BaseViewModel
import edu.vanderbilt.client.model.Airport
import edu.vanderbilt.client.web.BookingApi
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AirportViewModel(app: Application) : BaseViewModel(app) {

    private var _airports = MutableLiveData<List<Airport>>()
    val airports: LiveData<List<Airport>>
        get() = _airports

    var api: BookingApi

    init {
        // Start with an insecure api.
        api = getBookingService(getHttpClient())
    }

    fun listAirports() {
        launch {
            try {
                _airports.postValue(api.getAirports())
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    /**
     * Once you get a key, call this to rebuild the api so that
     * every request sent will have the interception add the
     * key to the HTTP header.
     */
    fun buildSecureApi(key: String) {
        api = getBookingService(getHttpClient(key))
    }

    private fun getBookingService(okHttpClient: OkHttpClient): BookingApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081")
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
                        .create()
                )
            )
            .build()
        return retrofit.create(BookingApi::class.java)
    }

    /**
     * The first time you call the service you won't have a
     * key  so you will build a client without a key. But
     * once you get the key, you discard that client and
     * build a new client that you always use for future
     * communication with the server.
     */
    private fun getHttpClient(key: String = ""): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = if (key.isBlank()) {
                    chain.request()
                } else {
                    chain.request()
                        .newBuilder()
                        .header("Authorization", key)
                        .build()
                }

                chain.proceed(request)
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}