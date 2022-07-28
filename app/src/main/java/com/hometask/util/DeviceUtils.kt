package com.hometask.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Locale
import java.util.regex.Pattern

/**
 * 与设备参数读取相关的工具类
 */
object DeviceUtils {
    private const val TAG = "DeviceUtils"
    private var mDevicesModel = ""

    /**
     * 当前Rom是否为小米Rom
     *
     * @return
     */
    fun isMiRom(): Boolean {
        val miUiVersionName = SystemProperties.get("ro.miui.ui.version.name")
        if (!TextUtils.isEmpty(miUiVersionName)) {
            return true
        }
        if (Build.MANUFACTURER != null) {
            val blank: String = replaceBlank(Build.MANUFACTURER)
            if (!TextUtils.isEmpty(blank)) {
                val lowerCase = blank.toLowerCase(Locale.ROOT)
                return !TextUtils.isEmpty(lowerCase) && lowerCase.contains("xiaomi")
            }
        }
        return false
    }

    private fun replaceBlank(str: String): String {
        var dest = ""
        if (!TextUtils.isEmpty(str)) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }

    fun getIPAddress(context: Context): String? {
        val info = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info != null && info.isConnected) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) { //当前使用2G/3G/4G网络
                try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress
                                    && inetAddress is Inet4Address) {
                                if ("null" != inetAddress.hostAddress
                                        && inetAddress.hostAddress != null) {
                                    return inetAddress.hostAddress.trim { it <= ' ' }
                                }
                            }
                        }
                    }
                } catch (e: SocketException) {
                    Log.e(TAG, "error", e)
                }
            } else if (info.type == ConnectivityManager.TYPE_WIFI) { //当前使用无线网络
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                try {
                    val wifiInfo = wifiManager.connectionInfo
                    return intIP2StringIP(wifiInfo.ipAddress)
                } catch (e: RuntimeException) {
                    Log.e(TAG, "error", e)
                }
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null
    }

    /**
     * cpu是否含arm64
     */
    fun isCpuHasArm64(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (bits in Build.SUPPORTED_64_BIT_ABIS) {
                Log.i(TAG, "cpubits : $bits")
                if (TextUtils.equals(bits, "arm64-v8a")) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    private fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }

    fun getDeviceModel(): String {
        if (TextUtils.isEmpty(mDevicesModel)) {
            mDevicesModel = Build.MODEL
        }
        return mDevicesModel
    }
}