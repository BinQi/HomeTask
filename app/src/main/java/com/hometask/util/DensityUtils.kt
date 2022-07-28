package com.hometask.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * Description: 像素转换工具
 */
object DensityUtils {
    /**
     * dp转px
     *
     * @param dpValue
     * @return
     */
    fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        // 像素没有小数；此处+0.5是为了解决向上取整，防止非整型的dp数被int取整后丢失精度
        // e.g. 1.5dp在3x的手机上应该按5px处理
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp
     *
     * @param pxValue
     * @return
     */
    fun px2dp(pxValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return pxValue / scale
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                context.resources.displayMetrics).toInt()
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }
}