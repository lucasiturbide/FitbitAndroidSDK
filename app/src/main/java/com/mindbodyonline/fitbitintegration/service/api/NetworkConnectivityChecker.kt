package com.mindbodyonline.fitbitintegration.service.api


interface NetworkConnectivityChecker {
    fun isConnected(): Boolean
}