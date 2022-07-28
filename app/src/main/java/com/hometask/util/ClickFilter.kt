package com.hometask.util

import android.animation.ObjectAnimator
import android.os.SystemClock
import android.view.View

/**
 * 快速点击过滤器
 */
class ClickFilter(
    private val intervalMs: Long = 500L,
    private val useGlobal: Boolean = false,
    private val listener: (View?) -> Unit
) : View.OnClickListener {

    private var lastClickTime = 0L
    private var objectAnimator: ObjectAnimator? = null
    var enabledClearAnimation: Boolean = true
    var enabledAnimation: Boolean = false

    override fun onClick(v: View?) {
        val curTime = SystemClock.elapsedRealtime()
        if (useGlobal && curTime - globalLastClickTime < intervalMs) {
            return
        }
        if (curTime - lastClickTime < intervalMs) {
            return
        }

        if (enabledAnimation && v != null) {
            objectAnimator?.target
            val animator = ButtonAnimUtils.getAnimator(v, objectAnimator)
            objectAnimator = animator
            animator.start()
        }

        lastClickTime = curTime
        globalLastClickTime = curTime
        listener.invoke(v)
    }

    fun clearAnimation() {
        if (enabledClearAnimation.not()) {
            return
        }
        objectAnimator?.let {
            if (it.isRunning) {
                it.end()
            }
        }
    }
}

var globalLastClickTime: Long = 0