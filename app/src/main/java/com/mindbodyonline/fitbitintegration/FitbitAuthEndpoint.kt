package com.mindbodyonline.fitbitintegration

import com.mindbodyonline.fitbitintegration.service.api.endpoint.AuthEndpoint
import com.mindbodyonline.fitbitintegration.service.api.endpoint.Environment

class FitbitAuthEndpoint: AuthEndpoint() {
    override fun oauthClientId(env: Environment): String {
        return ""
    }

    override fun oauthSecret(env: Environment): String {
        return ""
    }

}