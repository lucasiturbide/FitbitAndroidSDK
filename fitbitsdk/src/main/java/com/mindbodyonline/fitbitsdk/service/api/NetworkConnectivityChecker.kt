package com.mindbodyonline.fitbitsdk.service.api


interface NetworkConnectivityChecker {
    fun isConnected(): Boolean
}