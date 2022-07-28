package com.hometask.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.hometask.util.DensityUtils
import com.hometask.util.KeyboardStateHelper
import com.hometask.util.KeyboardUtils

/**
 *
 * @description 可以添加各个Drawable的监听
 */
class CompoundDrawableEditText @JvmOverloads constructor(context: Context,
                                                         attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : AppCompatEditText(context, attrs, defStyleAttr) {

    private val leftDrawable: Drawable?
        get() {
            return compoundDrawables[LEFT_INDEX]
        }

    private val rightDrawable: Drawable?
        get() {
            return compoundDrawables[RIGHT_INDEX]
        }

    private val topDrawable: Drawable?
        get() {
            return compoundDrawables[TOP_INDEX]
        }

    private val bottomDrawable: Drawable?
        get() {
            return compoundDrawables[BOTTOM_INDEX]
        }

    var leftListener: ICompoundDrawableClickListener? = null

    var rightListener: ICompoundDrawableClickListener? = null

    var topListener: ICompoundDrawableClickListener? = null

    var bottomListener: ICompoundDrawableClickListener? = null

    private var downY = -1f

    private val slideDownHideKeyboardDistance by lazy {
        DensityUtils.dp2px(SLIDE_DOWN_DISTANCE_FOR_HIDE_KEYBOARD_DP)
    }

    private var isSlideDownHideKeyboard = false

    private var isKeyboardShowed = false

    private val keyboardListener by lazy {
        object : KeyboardStateHelper.KeyboardStateListener {
            override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
                isKeyboardShowed = true
            }

            override fun onSoftKeyboardClosed() {
                isKeyboardShowed = false
            }
        }
    }

    companion object {
        const val LEFT_INDEX = 0
        const val TOP_INDEX = 1
        const val RIGHT_INDEX = 2
        const val BOTTOM_INDEX = 3
        private const val SLIDE_DOWN_DISTANCE_FOR_HIDE_KEYBOARD_DP = 40f
    }

    @SuppressLint("ClickableViewAccessibility")
    @Suppress("ReturnCount", "ComplexMethod")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || !isTouchInContent(event)) return super.onTouchEvent(event)
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
                val leftDw = leftDrawable
                val leftLis = leftListener
                if (leftDw != null && leftLis != null && event.x - paddingLeft - leftDw.bounds.width() <= 0) {
                    leftLis.onCompoundDrawableClicked()
                    return true
                }

                val rightDw = rightDrawable
                val rightLis = rightListener
                if (rightDw != null && rightLis != null && event.x + paddingRight + rightDw.bounds.width() >= width) {
                    rightLis.onCompoundDrawableClicked()
                    return true
                }

                val topDw = topDrawable
                val topList = topListener
                if (topDw != null && topList != null && event.y - paddingTop - topDw.bounds.height() <= 0) {
                    topList.onCompoundDrawableClicked()
                    return true
                }

                val bottomDw = bottomDrawable
                val bottomLis = bottomListener
                if (bottomDw != null && bottomLis != null
                        && event.y + paddingBottom + bottomDw.bounds.height() >= height) {
                    bottomLis.onCompoundDrawableClicked()
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isSlideDownHideKeyboard
                        && isKeyboardShowed
                        && event.y - downY > slideDownHideKeyboardDistance
                ) {
                    KeyboardUtils.hideKeyboard(this)
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    // 是否在edit编辑区域内
    private fun isTouchInContent(event: MotionEvent): Boolean {
        return (event.x - paddingLeft > 0) && (event.x + paddingRight <= width)
                && (event.y - paddingTop > 0) && (event.y + paddingBottom <= height)
    }

    /**
     * 配置下滑自动隐藏键盘，需要注册监听键盘[registerKeyboardListener]
     * 适当的时候反注册，以防内存泄漏[unRegisterKeyboardListener]
     * */
    fun configSlidDownHideKeyboard(isHide: Boolean) {
        isSlideDownHideKeyboard = isHide
    }

    /**
     * 注册监听键盘
     * */
    fun registerKeyboardListener(keyboardStateHelper: KeyboardStateHelper) {
        keyboardStateHelper.addSoftKeyboardStateListener(keyboardListener)
    }

    /**
     * 反注册监听键盘
     * */
    fun unRegisterKeyboardListener(keyboardStateHelper: KeyboardStateHelper) {
        keyboardStateHelper.removeSoftKeyboardStateListener(keyboardListener)
    }

    interface ICompoundDrawableClickListener {
        fun onCompoundDrawableClicked()
    }
}