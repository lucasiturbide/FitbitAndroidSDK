package com.mindbodyonline.fitbitsdk.service.storage

import com.mindbodyonline.fitbitsdk.service.models.auth.OAuthAccessToken


interface OAuthAccessTokenStorage {
    var token : OAuthAccessToken?
}