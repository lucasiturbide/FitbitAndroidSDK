package com.mindbodyonline.fitbitintegration

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mindbodyonline.fitbitintegration.service.FitbitService
import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken
import com.mindbodyonline.fitbitintegration.service.storage.SharedPreferenceTokenStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class FitbitLoginActivity : AppCompatActivity() {

    companion object {
        val ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNkxSTkwiLCJhdWQiOiIyMjdHNUwiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJ3YWN0IiwiZXhwIjoxNTI3MTcxOTk0LCJpYXQiOjE1MjcwODU1OTR9.bX-yHxax2vF8NKP_Fu2fVi6SJ5UEi9BrvAOYm6bX7aI"
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
        storage.token = OAuthAccessToken(ACCESS_TOKEN,
                TimeUnit.DAYS.toSeconds(4).toInt(),
                "",
                "",
                "",
                "")
        val fService = FitbitService(sharedPreferences)
        fService.getUserService().badges().enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                Log.d("Service", "Response: " + response.toString())
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Log.d("Service", "Error: " + t?.message)
            }

        })
    }
}
