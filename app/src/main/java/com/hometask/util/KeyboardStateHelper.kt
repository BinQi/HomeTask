package com.hometask.util

import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import java.util.LinkedList

/**
 * 负责处理键盘的状态更新
 */
class KeyboardStateHelper @JvmOverloads constructor(
    private var activityRootView: View?,
    var isSoftKeyboardOpened: Boolean = false,
) : OnGlobalLayoutListener {
    interface KeyboardStateListener {
        fun onSoftKeyboardOpened(keyboardHeightInPx: Int)
        fun onSoftKeyboardClosed()
    }

    companion object {
        private const val KEYBOARD_OPEN_MIN_HEIGHT = 250
        private const val KEYBOARD_HIDE_MAX_HEIGHT = 100
    }

    private val listeners: MutableList<KeyboardStateListener> = LinkedList()
    private var mFirstHeight = 0

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    private var lastSoftKeyboardHeightInPx = 0

    init {
        activityRootView?.viewTreeObserver?.addOnGlobalLayoutListener(this)
    }

    fun recycler() {
        listeners.clear()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activityRootView?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
        } else {
            activityRootView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        }
        activityRootView = null
    }


    override fun onGlobalLayout() {
        val rect = Rect()
        activityRootView?.getWindowVisibleDisplayFrame(rect)
        val h = rect.height()
        if (mFirstHeight == 0) {
            mFirstHeight = h
        } else {
            val hDiff = mFirstHeight - h
            if (!isSoftKeyboardOpened && hDiff >= KEYBOARD_OPEN_MIN_HEIGHT) {
                isSoftKeyboardOpened = true
                notifyOnSoftKeyboardOpened(hDiff)
            } else if (isSoftKeyboardOpened && hDiff < KEYBOARD_HIDE_MAX_HEIGHT) {
                isSoftKeyboardOpened = false
                notifyOnSoftKeyboardClosed()
            }
        }
    }

    fun addSoftKeyboardStateListener(
            listener: KeyboardStateListener) {
        listeners.add(listener)
    }

    fun removeSoftKeyboardStateListener(
            listener: KeyboardStateListener?) {
        listeners.remove(listener)
    }

    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        lastSoftKeyboardHeightInPx = keyboardHeightInPx
        for (listener in listeners) {
            listener.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        for (listener in listeners) {
            listener.onSoftKeyboardClosed()
        }
    }
}