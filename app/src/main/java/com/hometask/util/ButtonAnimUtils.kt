package com.hometask.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

/**
 * 按钮动画工具类
 */
object ButtonAnimUtils {
    private const val ANIM_DURATION_MS = 200L

    @Suppress("MagicNumber")
    private val animValues = floatArrayOf(1.0f, 0.8f, 1.0f) // 动画变化过程

    private fun createScaleValues(orgScale: Float): FloatArray {
        return animValues.map { it * orgScale }.toFloatArray()
    }

    @Suppress("SpreadOperator")
    fun createAnimator(view: View): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", *createScaleValues(view.scaleX))
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", *createScaleValues(view.scaleY))
        val animator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
        animator.duration = ANIM_DURATION_MS
        return animator
    }

    fun getAnimator(view: View, oldAnimator: ObjectAnimator?): ObjectAnimator {
        if (oldAnimator == null) {
            return createAnimator(view)
        }
        if (oldAnimator.target == view) {
            if (oldAnimator.isRunning) {
                oldAnimator.end()
            }
            return oldAnimator
        }
        return createAnimator(view)
    }
}