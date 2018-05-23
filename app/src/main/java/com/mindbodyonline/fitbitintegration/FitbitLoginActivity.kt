package com.mindbodyonline.fitbitintegration

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mindbodyonline.fitbitintegration.service.FitbitService
import com.mindbodyonline.fitbitintegration.service.models.UserContainer
import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken
import com.mindbodyonline.fitbitintegration.service.storage.SharedPreferenceTokenStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class FitbitLoginActivity : AppCompatActivity() {

    companion object {
        val ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNkxSTkwiLCJhdWQiOiIyMjdHNUwiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJ3YWN0IHdwcm8iLCJleHAiOjE1MjcxODU4MTgsImlhdCI6MTUyNzA5OTQxOH0.NPwMmQI0g0IHZaZfNCh4pMf1iZHg8wSLnP4aU6PrYXo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitbit_login)
    }

    override fun onResume() {
        super.onResume()
//        var apiClient = ApiClient("oauth2")
//        apiClient.setAccessToken(ACCESS_TOKEN)
//
//        var bodyAndWeightApi = apiClient.createService(BodyAndWeightApi::class.java)
//        var result = bodyAndWeightApi.weight_0("2018-05-01", "2018-05-15").enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>?, response: Response<String>?) {
//                Log.d("API", response.toString())
//            }
//
//            override fun onFailure(call: Call<String>?, t: Throwable?) {
//                Log.d("API", t.toString())
//            }
//
//        })
        val sharedPreferences = getSharedPreferences("Fitbit", Context.MODE_PRIVATE)
        val storage = SharedPreferenceTokenStorage(sharedPreferences)
        storage.token = null
        storage.token = OAuthAccessToken(ACCESS_TOKEN,
                TimeUnit.DAYS.toSeconds(4).toInt(),
                "",
                "",
                "",
                "")
        val fService = FitbitService(sharedPreferences)
        fService.getUserService().profile().enqueue(object: Callback<UserContainer>{
            override fun onResponse(call: Call<UserContainer>?, response: Response<UserContainer>?) {
                Log.d("Service", "Response: " + response?.body()?.user?.dateOfBirth)
            }

            override fun onFailure(call: Call<UserContainer>?, t: Throwable?) {
                Log.d("Service", "Error: " + t?.message)
            }

        })
    }
}
