package com.mindbodyonline.fitbitintegration.service.api

import com.mindbodyonline.fitbitintegration.service.models.auth.AuthorizeCodeTokenModel
import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken
import retrofit2.Call
import retrofit2.http.*

interface TokenService{

    @Headers("Content-Type: application/json")
    @POST("oauth2/revoke")
    fun revokeToken(@Header("Authorization") authorization: String,
                    @Query("token") token: String): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("oauth/token")
    fun authorizeCode(@Body authorization: AuthorizeCodeTokenModel): Call<OAuthAccessToken>
}