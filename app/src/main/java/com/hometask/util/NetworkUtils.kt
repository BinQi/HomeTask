package com.hometask.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.telephony.TelephonyManager

object NetworkUtils {
    private const val TAG = "NetworkUtil"

    /**
     * None network.
     */
    const val NETWORK_TYPE_NONE = -2

    /**
     * Unknown network, but available.
     */
    const val NETWORK_TYPE_UNKNOWN = -1

    /**
     * Wi-Fi network.
     */
    const val NETWORK_TYPE_WIFI = 0

    /**
     * Mobile network, but unknown.
     */
    const val NETWORK_TYPE_UNKNOWN_MOBILE = 1

    /**
     * 2G network, including 2.5G.
     */
    const val NETWORK_TYPE_2G = 2

    /**
     * 3G network, including 3.5G.
     */
    const val NETWORK_TYPE_3G = 3

    /**
     * 4G network.
     */
    const val NETWORK_TYPE_4G = 4

    /**
     * APN name for wifi.
     */
    const val APN_NAME_WIFI = "wifi"
    private val APN_URI = Uri.parse("content://telephony/carriers/preferapn")
    private const val APN_COLUMN_NAME = "apn"
    private const val PROPERTY_DNS_PRIMARY = "net.dns1"
    private const val PROPERTY_DNS_SECONDARY = "net.dns2"

    const val HTTP_URL = "http"


    /**
     * Check whether current network is connected.
     *
     * @param context Application context.
     * @return Whether current network is connected.
     */
    fun isNetworkConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * Check whether wifi network is connected right now.
     *
     * @param context Application context.
     * @return Whether wifi network is connected.
     */
    fun isWifiConnected(context: Context): Boolean {
        val activeNetworkInfo = getActiveNetworkInfo(context)
        return (activeNetworkInfo != null
                && activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI)
    }

    /**
     * Check whether mobile network is connected right now.
     *
     * @param context Application context.
     * @return Whether mobile network is connected.
     */
    fun isMobileConnected(context: Context): Boolean {
        val activeNetworkInfo = getActiveNetworkInfo(context)
        return (activeNetworkInfo != null
                && activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE)
    }

    /**
     * Returns details about the currently active default data network. When connected, this network
     * is the default route for outgoing connections. You should always check [ ][NetworkInfo.isConnected] before initiating network traffic. This may return `null`
     * when there is no default network.
     *
     * @param context Application context.
     * @return a [NetworkInfo] object for the current default network or `null` if no
     * network default network is currently active
     *
     *
     *
     * This method requires the call to hold the permission
     * [android.Manifest.permission.ACCESS_NETWORK_STATE].
     */
    @SuppressLint("MissingPermission")
    fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        return try {
            val connMgr = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connMgr.activeNetworkInfo
        } catch (e: Throwable) {
            null
        }
    }

    /**
     * Returns type of current active network.
     *
     * @param context Application context.
     * @return type of current active network, the alternation is [.NETWORK_TYPE_WIFI], [ ][.NETWORK_TYPE_4G], [.NETWORK_TYPE_3G], [.NETWORK_TYPE_2G], [ ][.NETWORK_TYPE_UNKNOWN_MOBILE], [.NETWORK_TYPE_UNKNOWN], [.NETWORK_TYPE_NONE].
     */
    fun getActiveNetworkType(context: Context): Int {
        return getNetworkType(context, getActiveNetworkInfo(context))
    }

    /**
     * Returns type of corresponding network info.
     *
     * @param context Application context.
     * @param info Network info.
     * @return type of current active network, the alternation is [.NETWORK_TYPE_WIFI], [ ][.NETWORK_TYPE_4G], [.NETWORK_TYPE_3G], [.NETWORK_TYPE_2G], [ ][.NETWORK_TYPE_UNKNOWN_MOBILE], [.NETWORK_TYPE_UNKNOWN], [.NETWORK_TYPE_NONE].
     */
    fun getNetworkType(context: Context?, info: NetworkInfo?): Int {
        if (info != null) {
            if (info.type == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_TYPE_WIFI
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                val subType = info.subtype
                return when (subType) {
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_1xRTT,
                    TelephonyManager.NETWORK_TYPE_IDEN -> {
                        NETWORK_TYPE_2G
                    }
                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_HSPA -> {
                        NETWORK_TYPE_3G
                    }
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_HSPAP,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA -> {
                        NETWORK_TYPE_3G
                    }
                    TelephonyManager.NETWORK_TYPE_LTE,
                    TelephonyManager.NETWORK_TYPE_EHRPD -> {
                        NETWORK_TYPE_4G
                    }
                    else -> NETWORK_TYPE_UNKNOWN_MOBILE
                }
            }
            return NETWORK_TYPE_UNKNOWN
        }
        return NETWORK_TYPE_NONE
    }

    fun isHttpUrl(url: String?): Boolean {
        return url?.startsWith(HTTP_URL, true) ?: false
    }

}