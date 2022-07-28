package com.hometask.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hometask.app.R

/**
 * @description 通用dialog弹框的工厂类
 */
object DialogFactory {

    private val dialogTypeMap = mapOf(
        DialogType.OneBtn to R.layout.dialog_one_button_normal_layout,
        DialogType.TwoHorizontalBtn to R.layout.dialog_two_horizontal_button_normal_layout,
    )

    /**
     * 创建通用的dialogBuilder
     * @param context
     * @param dialogType 见[DialogType]
     */
    fun createBuilder(context: Context, dialogType: DialogType): CustomDialogBuilder {
        return CustomDialogBuilder(context).setCustomView(dialogTypeMap[dialogType]
            ?: R.layout.dialog_one_button_normal_layout)
    }
}

enum class DialogType {
    /**
     * 单个按钮的通用弹框，包含以下元素：
     * 标题[android.R.id.title]
     * 内容[android.R.id.content]
     * 确定按钮(Positive)[android.R.id.button1] style可配置，[PositiveStyle]
     * 关闭按钮(右上角)[android.R.id.closeButton]
     */
    OneBtn,

    /**
     * 两个横排按钮的通用弹框，包含以下元素：
     * 标题[android.R.id.title]
     * 内容[android.R.id.content]
     * 确定按钮(Positive)[android.R.id.button1] style可配置，[PositiveStyle]
     * 取消按钮(Negative)[android.R.id.button2]
     * 关闭按钮(右上角)[android.R.id.closeButton]
     *
     * 按钮顺序从左到右：取消按钮(Negative)，确定按钮(Positive)
     */
    TwoHorizontalBtn,
}

enum class PositiveStyle {
    /**
     * positive按钮的默认样式，背景是主题色，文字是主色
     * [R.drawable.custom_dialog_confirm_bg_def_style]
     * [R.color.main_color_c2]
     */
    Default,

    /**
     * positive按钮的警告样式，背景是警告色，文字为白色
     * [R.drawable.custom_dialog_warn_bg_def_style]
     * [R.color.white]
     */
    Warn;

    fun applyTo(view: TextView?) {
        when (this) {
            Warn -> {
                view?.apply {
                    setBackgroundResource(R.drawable.custom_dialog_warn_bg_def_style)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            else -> {
                view?.apply {
                    setBackgroundResource(R.drawable.custom_dialog_confirm_bg_def_style)
                    setTextColor(ContextCompat.getColor(context, R.color.main_color_c2))
                }
            }
        }
    }
}

/**
 * 获取CustomView
 */
fun Dialog.getContentView(): View? {
    return findViewById(android.R.id.content)
}

/**
 * 获取确定按钮
 */
fun Dialog.getPositiveBtn(): TextView? {
    return findViewById(android.R.id.button1)
}

/**
 * 获取取消按钮
 */
fun Dialog.getNegativeBtn(): TextView? {
    return findViewById(android.R.id.button2)
}

/**
 * 获取中立按钮
 */
fun Dialog.getNeutralBtn(): TextView? {
    return findViewById(android.R.id.button3)
}

/**
 * 获取关闭按钮(右上角)
 */
fun Dialog.getCloseBtn(): View? {
    return findViewById(android.R.id.closeButton)
}