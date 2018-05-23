package com.mindbodyonline.fitbitintegration.service.storage

import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken


interface OAuthAccessTokenStorage {
    var token : OAuthAccessToken?
}