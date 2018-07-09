package com.mindbodyonline.fitbitintegration

import com.mindbodyonline.fitbitintegration.service.api.endpoint.AuthEndpoint
import com.mindbodyonline.fitbitintegration.service.api.endpoint.Environment

class FitbitAuthEndpoint: AuthEndpoint() {
    override fun oauthClientId(env: Environment): String {
        return "8c9c85d005054529ff0f8f715df3623b"
    }

    override fun oauthSecret(env: Environment): String {
        return ""
    }

}