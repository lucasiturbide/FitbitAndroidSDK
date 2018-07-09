package com.mindbodyonline.fitbitintegration

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.mindbodyonline.fitbitintegration.authentication.AuthenticationHandler
import com.mindbodyonline.fitbitintegration.authentication.AuthenticationManager
import com.mindbodyonline.fitbitintegration.authentication.AuthenticationResult
import com.mindbodyonline.fitbitintegration.service.FitbitService
import com.mindbodyonline.fitbitintegration.service.models.UserProfile
import kotlinx.android.synthetic.main.activity_fitbit_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FitbitLoginActivity : AppCompatActivity(), AuthenticationHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitbit_login)
    }

    override fun onResume() {
        super.onResume()
//        val storage = SharedPreferenceTokenStorage(sharedPreferences)
//        storage.token = null
//        storage.token = OAuthAccessToken(ACCESS_TOKEN,
//                TimeUnit.DAYS.toSeconds(4).toInt(),
//                "",
//                arrayListOf(),
//                "",
//                "")
        button.setOnClickListener { _ -> AuthenticationManager.login(this) }
    }

    override fun onAuthFinished(authenticationResult: AuthenticationResult?) {
        if (authenticationResult != null) {
            if (authenticationResult.isSuccessful) {
                onLoggedIn()
            } else {
                displayAuthError(authenticationResult)
            }
        }
    }

    fun onLoggedIn(){
        val sharedPreferences = getSharedPreferences("Fitbit", Context.MODE_PRIVATE)
        val fService = FitbitService(sharedPreferences)
        fService.getUserService().profile().enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>?, response: Response<UserProfile>?) {
                Log.d("Service", "Response: " + response?.body()?.user?.dateOfBirth)
            }

            override fun onFailure(call: Call<UserProfile>?, t: Throwable?) {
                Log.d("Service", "Error: " + t?.message)
            }

        })
    }

    private fun displayAuthError(authenticationResult: AuthenticationResult) {
        var message = ""

        when (authenticationResult.status) {
            AuthenticationResult.Status.dismissed -> message = getString(R.string.login_dismissed)
            AuthenticationResult.Status.error -> message = authenticationResult.errorMessage
            AuthenticationResult.Status.missing_required_scopes -> {
                val missingScopes = authenticationResult.missingScopes
                val missingScopesText = TextUtils.join(", ", missingScopes)
                message = getString(R.string.missing_scopes_error) + missingScopesText
            }
        }

        AlertDialog.Builder(this)
                .setTitle(R.string.login_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, id -> })
                .create()
                .show()
    }

}
