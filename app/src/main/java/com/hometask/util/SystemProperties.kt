package com.hometask.util

import java.lang.reflect.Method

object SystemProperties {
    // String SystemProperties.get(String key){}
    operator fun get(key: String?): String? {
        init()
        var value: String? = null
        try {
            value = mGetMethod!!.invoke(mClassType, key) as String
        } catch (ignored: Exception) {
        }
        return value
    }

    //int SystemProperties.get(String key, int def){}
    fun getInt(key: String?, def: Int): Int {
        init()
        var value = def
        try {
            val v = mGetIntMethod!!.invoke(mClassType, key, def) as Int
            value = v
        } catch (ignored: Exception) {
        }
        return value
    }

    val sdkVersion: Int
        get() = getInt("ro.build.version.sdk", -1)

    //-------------------------------------------------------------------
    private var mClassType: Class<*>? = null
    private var mGetMethod: Method? = null
    private var mGetIntMethod: Method? = null
    private fun init() {
        try {
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties")
                mGetMethod = mClassType?.getDeclaredMethod("get", String::class.java)
                mGetIntMethod = mClassType?.getDeclaredMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            }
        } catch (ignored: Exception) {
        }
    }
}