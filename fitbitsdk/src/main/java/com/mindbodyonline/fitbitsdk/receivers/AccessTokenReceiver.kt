package com.mindbodyonline.fitbitsdk.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mindbodyonline.fitbitsdk.service.models.auth.OAuthAccessToken

class AccessTokenReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

    }

    private fun saveAccessToken(accessToken: OAuthAccessToken){

    }
}