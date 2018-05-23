package com.mindbodyonline.fitbitintegration.viewmodels

import android.arch.lifecycle.ViewModel
import android.net.Uri

class FitbitViewModel: ViewModel() {
    companion object {
        val AUTHORIZATION_CODE_GRANT_URL = "https://www.fitbit.com/oauth2/authorize"
        val QUERY_PARAM_RESPONSE_TYPE = "response_type"
        val TYPE_CODE = "code"
        val QUERY_PARAM_CLIENT_ID = "client_id"
        val QUERY_PARAM_REDIRECT_URL = "redirect_uri"
        val QUERY_PARAM_SCOPE = "scope"
        val SCOPE = arrayOf( "activity", "nutrition", "heartrate", "location", "nutrition", "profile", "settings", "sleep", "social", "weight")
    }

    fun requestAccess(){
        val uriBuilder = Uri.Builder()
                .appendQueryParameter(QUERY_PARAM_RESPONSE_TYPE, TYPE_CODE)
                .appendQueryParameter(QUERY_PARAM_CLIENT_ID, "")
    }
}