package com.mindbodyonline.fitbitintegration.service.models.auth

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.mindbodyonline.fitbitintegration.authentication.Scope
import kotlinx.android.parcel.Parcelize
import java.util.*

@JsonAdapter(OAuthAccessTokenTypeAdapter::class)
@Parcelize
class OAuthAccessToken(
        var access_token: String?,
        var expires_in: Int?,
        var refresh_token: String?,
        var scopes: List<Scope>?,
        var token_type: String?,
        var user_id: String?) : Parcelable{

    @Transient
    private val expiration: Calendar

    init {
        this.expiration = expires_in?.let {
            Calendar.getInstance().apply {
                this.add(Calendar.SECOND, it)
            }
        } ?: Calendar.getInstance()
    }

    fun needsRefresh(): Boolean {
        //Force a refresh up to 10 minutes before the expiration time
        val windowPeriod = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 10)
        }

        return expiration.before(windowPeriod)
    }
}