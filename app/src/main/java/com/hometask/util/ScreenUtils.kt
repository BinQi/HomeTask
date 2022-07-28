package com.hometask.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.provider.Settings
import android.view.WindowManager
import kotlin.math.max

/**
 * 屏幕相关操作工具类。
 */
object ScreenUtils {
    /**
     * 获取屏幕宽度。
     *
     * @return 返回屏幕宽度。
     */
    @JvmStatic
    @Deprecated("不建议用这个，因为获取到的不是真实的屏幕宽度，有可能是屏幕高度",
        ReplaceWith("ScreenUtils.getScreenWidth()")
    )
    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    /**
     * 获取屏幕宽度。
     *
     * @return 返回屏幕宽度。
     */
    fun getScreenWidth(context: Context): Int {
        val resources = context.resources
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resources.displayMetrics.heightPixels
        } else {
            resources.displayMetrics.widthPixels
        }
    }

    /**
     * 获取屏幕高度。
     *
     * @return 返回屏幕高度。
     */
    fun getScreenHeight(context: Context): Int {
        val resources = context.resources
        val screenHeight = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resources.displayMetrics.widthPixels
        } else {
            resources.displayMetrics.heightPixels
        }
        return screenHeight + getMiSupplementHeight(context)
    }

    /**
     * 获取全部屏幕宽度(物理宽度)
     */
    fun getFullScreenWidth(context: Context): Int {
        return max(getScreenWidth(context), getDisplayWidth(context))
    }

    /**
     * 获取全部屏幕高度(物理高度，包括流海屏的区域)
     */
    fun getFullScreenHeight(context: Context): Int {
        return max(getScreenHeight(context), getDisplayHeight(context))
    }

    /**
     * 获取显示宽度
     */
    fun getDisplayWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            ?: return -1
        val display = wm.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return size.x
    }

    /**
     * 获取显示高度
     */
    fun getDisplayHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            ?: return -1
        val display = wm.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return size.y
    }

    /**
     * 获取需要补充的高度
     * @param context
     * @return
     */
    private fun getMiSupplementHeight(context: Context): Int {
        var result = 0
        //是否是小米系统，不是小米系统则不需要补充高度
        if (DeviceUtils.isMiRom()) {
            if (Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0) {
                //如果虚拟按键没有显示，则需要补充虚拟按键高度到屏幕高度
                val res = context.resources
                val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId)
                }
            }
        }
        return result
    }
}