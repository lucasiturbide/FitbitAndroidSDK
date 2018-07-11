package com.mindbodyonline.fitbitsdk.service.api.okhttp.interceptor

import com.mindbodyonline.fitbitsdk.service.api.endpoint.AuthEndpoint
import com.mindbodyonline.fitbitsdk.service.api.endpoint.Environment
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthenticationInterceptor(private val authEndpoint: AuthEndpoint, private val environment: Environment) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val header = authEndpoint.basicHeader(environment)
        val authedRequest = request.newBuilder().addHeader(header.first, header.second).build()
        return chain.proceed(authedRequest)
    }
}