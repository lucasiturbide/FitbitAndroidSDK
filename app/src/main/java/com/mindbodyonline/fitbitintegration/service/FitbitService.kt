package com.mindbodyonline.fitbitintegration.service

import android.content.SharedPreferences
import android.util.Log
import com.mindbodyonline.fitbitintegration.FitbitAuthEndpoint
import com.mindbodyonline.fitbitintegration.gsonhelper.SafeGson
import com.mindbodyonline.fitbitintegration.service.api.*
import com.mindbodyonline.fitbitintegration.service.api.endpoint.AuthEndpoint
import com.mindbodyonline.fitbitintegration.service.api.endpoint.Endpoint
import com.mindbodyonline.fitbitintegration.service.api.endpoint.Environment
import com.mindbodyonline.fitbitintegration.service.api.okhttp.interceptor.AuthenticationInterceptor
import com.mindbodyonline.fitbitintegration.service.api.okhttp.interceptor.Authenticator
import com.mindbodyonline.fitbitintegration.service.api.okhttp.retrofit.RetrofitRefreshTokenService
import com.mindbodyonline.fitbitintegration.service.storage.SharedPreferenceTokenStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FitbitService(sharedPreferences: SharedPreferences){

    var service: Retrofit


    init{
        val authEndpoint = FitbitAuthEndpoint()
        val endpoint = object: Endpoint(){
            override val authEndpoint: AuthEndpoint
                get() = authEndpoint
        }
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        val oAuthDataService = OAuthDataService(
                SharedPreferenceTokenStorage(sharedPreferences),
                RetrofitRefreshTokenService(
                        Environment.PRODUCTION,
                        endpoint,
                        OkHttpClient.Builder()
                                .addInterceptor(loggingInterceptor)
                ),
                object: NetworkConnectivityChecker {
                    override fun isConnected(): Boolean {
                        return true
                    }

                },
                object: TokenDroppedListener{
                    override fun onTokenDropped() {
                        Log.d("Authenticator","Token Dropped")
                    }

                }
        )
        val client = OkHttpClient.Builder()
                .authenticator(Authenticator(
                        oAuthDataService
                ))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthenticationInterceptor(oAuthDataService))
                .build()

        service = Retrofit.Builder()
                .baseUrl("https://api.fitbit.com/1/")
                .addConverterFactory(GsonConverterFactory.create(SafeGson.getSingleton().gsonInstance))
                .client(client)
                .build()
    }

    fun getBodyAndWeightService(): BodyAndWeightService{
        return service.create(BodyAndWeightService::class.java)
    }

    fun getUserService(): UserService{
        return service.create(UserService::class.java)
    }

    fun getTokenService(): TokenService{
        return service.create(TokenService::class.java)
    }
}