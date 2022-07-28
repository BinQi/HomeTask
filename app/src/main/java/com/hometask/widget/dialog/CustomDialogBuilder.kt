package com.hometask.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.hometask.app.R
import com.hometask.util.DensityUtils
import com.hometask.util.ScreenUtils
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 自定义布局dialog builder，只要布局xml添加以下id则会自动关联点击和内容设置，如果customView没有设置则使用默认样式
 *
 * * 外层Layout(customView)[android.R.id.content]
 * * 标题[android.R.id.title]
 * * 内容[android.R.id.message]
 * * 确定按钮(Positive)[android.R.id.button1] style可配置，[PositiveStyle]
 * * 取消按钮(Negative)[android.R.id.button2]
 * * 中立按钮(Neutral)[android.R.id.button3]
 * * 关闭按钮(右上角)[android.R.id.closeButton]
 *
 * # 使用方法：
 * ```
 * CustomDialogBuilder(context)
 *   .setCustomView(R.layout.my_layout)
 *   .setTitle(R.string.my_title)
 *   .setMessage(R.string.my_desc)
 *   .setPositiveBtn(R.string.i_known)
 *   .setPositiveStyle(PositiveStyle.Default)
 *   .build()
 *   .show()
 * ```
 */
class CustomDialogBuilder(private val ctx: Context) {

    private val params = DialogParams()

    fun setCustomView(view: View): CustomDialogBuilder {
        params.customView = view
        return this
    }

    fun getCustomView(): View? {
        return params.customView
    }

    fun setCustomView(@LayoutRes resId: Int): CustomDialogBuilder {
        return setCustomView(LayoutInflater.from(ctx).inflate(resId, null))
    }

    fun setTitle(@StringRes resId: Int): CustomDialogBuilder {
        return setTitle(ctx.getString(resId))
    }

    fun setTitle(title: String): CustomDialogBuilder {
        params.title = title
        return this
    }

    fun setMessage(@StringRes resId: Int): CustomDialogBuilder {
        return setMessage(ctx.getString(resId))
    }

    fun setMessage(content: String): CustomDialogBuilder {
        params.message = content
        return this
    }

    fun setDialogStyle(@StyleRes resId: Int): CustomDialogBuilder {
        params.dialogStyleResId = resId
        return this
    }

    fun setCancelable(cancelable: Boolean): CustomDialogBuilder {
        params.cancelable = cancelable
        return this
    }

    fun setOnCancelListener(listener: DialogInterface.OnCancelListener): CustomDialogBuilder {
        params.onCancelListener = listener
        return this
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener): CustomDialogBuilder {
        params.onDismissListener = listener
        return this
    }

    fun setOnShowListener(listener: OnShowListener): CustomDialogBuilder {
        params.onShowListener = listener
        return this
    }

    fun setPositiveBtn(
        @StringRes resId: Int,
        lst: DialogInterface.OnClickListener? = null
    ): CustomDialogBuilder {
        return setPositiveBtn(ctx.getString(resId), lst)
    }

    fun setPositiveBtn(
        btnStr: String,
        lst: DialogInterface.OnClickListener? = null
    ): CustomDialogBuilder {
        params.positiveBtnName = btnStr
        return setPositiveBtnClickListener(lst)
    }

    fun setPositiveStyle(style: PositiveStyle): CustomDialogBuilder {
        params.positiveStyle = style
        return this
    }

    fun setPositiveBtnClickListener(lst: DialogInterface.OnClickListener?): CustomDialogBuilder {
        params.positiveBtnClickListener = lst
        return this
    }

    fun setNegativeBtn(
        @StringRes resId: Int,
        lst: DialogInterface.OnClickListener? = null
    ): CustomDialogBuilder {
        return setNegativeBtn(ctx.getString(resId), lst)
    }

    fun setNegativeBtn(
        btnStr: String,
        lst: DialogInterface.OnClickListener? = null
    ): CustomDialogBuilder {
        params.negativeBtnName = btnStr
        return setNegativeBtnClickListener(lst)
    }

    fun setNegativeBtnClickListener(lst: DialogInterface.OnClickListener?): CustomDialogBuilder {
        params.negativeBtnClickListener = lst
        return this
    }

    fun setNeutralBtn(
        @StringRes resId: Int,
        lst: DialogInterface.OnClickListener? = null
    ): CustomDialogBuilder {
        return setNeutralBtn(ctx.getString(resId), lst)
    }

    fun setNeutralBtn(
        btnStr: String,
        lst: DialogInterface.OnClickListener? = null
    ): CustomDialogBuilder {
        params.neutralBtnName = btnStr
        return setNeutralBtnClickListener(lst)
    }

    fun setNeutralBtnClickListener(lst: DialogInterface.OnClickListener?): CustomDialogBuilder {
        params.neutralBtnClickListener = lst
        return this
    }

    fun setCloseBtnState(isShow: Boolean): CustomDialogBuilder {
        params.showCloseBtn = isShow
        return this
    }

    fun setCloseBtnClickListener(lst: DialogInterface.OnCancelListener?): CustomDialogBuilder {
        params.closeBtnClickListener = lst
        return this
    }

    fun setDialogWidth(width: Int): CustomDialogBuilder {
        params.dialogWidth = width
        return this
    }

    /**
     * 设置宽度percent
     */
    fun setDialogWidthPercent(percent: Float): CustomDialogBuilder {
        params.dialogWidthPercent = percent
        return this
    }

    fun build(): Dialog {
        val dialog = if (params.dialogStyleResId == 0) {
            Dialog(ctx)
        } else {
            Dialog(ctx, params.dialogStyleResId)
        }
        initDialog(dialog)
        return dialog
    }

    private fun initDialog(dialog: Dialog) {
        initDefaultStyle()

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        params.customView?.let {
            initViewStyle(it)
            initListener(dialog, it)
            dialog.setContentView(it)
        }

        // 应用样式
        params.positiveStyle.applyTo(dialog.getPositiveBtn())

        params.onCancelListener?.let {
            dialog.setOnCancelListener(it)
        }

        params.onDismissListener?.let {
            dialog.setOnDismissListener(it)
        }

        params.onShowListener?.let {
            dialog.setOnShowListener(it)
        }

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            decorView.setPadding(0, 0, 0, 0)
            val wlp: WindowManager.LayoutParams = attributes
            wlp.gravity = Gravity.CENTER
            wlp.width = params.dialogWidth
            wlp.dimAmount = DEF_DIM_AMOUNT
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = wlp
            setWindowAnimations(R.style.dialogWindowAnim)
        }
        dialog.setCancelable(params.cancelable)
    }

    private fun initCancelBtn(dialog: DialogInterface, rootView: View) {
        val closeBtn: View? = rootView.findViewById(android.R.id.closeButton)
        if (params.showCloseBtn.not()) {
            closeBtn?.visibility = View.GONE
            return
        }
        closeBtn?.visibility = View.VISIBLE
        closeBtn?.setOnClickListener {
            params.closeBtnClickListener?.onCancel(dialog)
            dialog.dismiss()
        }
    }

    private fun initBtn(
        dialog: DialogInterface,
        view: View,
        @IdRes btnId: Int,
        pos: Int,
        lst: DialogInterface.OnClickListener?
    ) {
        val positiveBtn: View? = view.findViewById(btnId)
        positiveBtn?.setOnClickListener { btn ->
            lst?.let {
                btn.visibility = View.VISIBLE
                it.onClick(dialog, pos)
                return@setOnClickListener
            }
            dialog.dismiss()
        }
    }

    private fun initListener(dialog: DialogInterface, rootView: View) {
        initCancelBtn(dialog, rootView)
        initBtn(
            dialog,
            rootView,
            android.R.id.button1,
            DialogInterface.BUTTON_POSITIVE,
            params.positiveBtnClickListener
        )
        initBtn(
            dialog,
            rootView,
            android.R.id.button2,
            DialogInterface.BUTTON_NEGATIVE,
            params.negativeBtnClickListener
        )
        initBtn(
            dialog,
            rootView,
            android.R.id.button3,
            DialogInterface.BUTTON_NEUTRAL,
            params.neutralBtnClickListener
        )
    }

    private fun initText(rootView: View, @IdRes resId: Int, text: String?) {
        if (text == null) {
            return
        }
        (rootView.findViewById(resId) as? TextView)?.let {
            it.visibility = View.VISIBLE
            it.text = text
        }
    }

    private fun initViewStyle(rootView: View) {
        initText(rootView, android.R.id.title, params.title)
        initText(rootView, android.R.id.message, params.message)
        initText(rootView, android.R.id.button1, params.positiveBtnName)
        initText(rootView, android.R.id.button2, params.negativeBtnName)
        initText(rootView, android.R.id.button3, params.neutralBtnName)
    }

    /**
     * 如果customView没有设置，则使用默认样式
     */
    private fun initDefaultStyle() {
        when {
            params.dialogWidth > 0 -> {
                // nothing
            }
            params.dialogWidthPercent > 0 -> {
                val width =
                    (ScreenUtils.getFullScreenWidth(ctx) * params.dialogWidthPercent).roundToInt()
                params.dialogWidth = width
            }
            else -> {
                val width = (ScreenUtils.getFullScreenWidth(ctx) * DEF_WIDTH_PERCENT).roundToInt()
                val maxWidth = DensityUtils.dp2px(DEF_MAX_WIDTH_DP)
                params.dialogWidth = min(width, maxWidth)
            }
        }
        if (params.customView == null) {
            params.customView =
                LayoutInflater.from(ctx).inflate(R.layout.dialog_one_button_normal_layout, null)
        }
    }

    private class DialogParams {

        var customView: View? = null

        var title: String? = null

        var message: String? = null

        var positiveBtnName: String? = null

        var positiveStyle: PositiveStyle = PositiveStyle.Default

        var negativeBtnName: String? = null

        var neutralBtnName: String? = null

        var showCloseBtn: Boolean = false

        var dialogWidth: Int = 0

        /**
         * 宽度百分比，在没有设置[dialogWidth]时才有效
         */
        var dialogWidthPercent: Float = 0f

        var closeBtnClickListener: DialogInterface.OnCancelListener? = null

        var positiveBtnClickListener: DialogInterface.OnClickListener? = null

        var negativeBtnClickListener: DialogInterface.OnClickListener? = null

        var neutralBtnClickListener: DialogInterface.OnClickListener? = null

        @StyleRes
        var dialogStyleResId: Int = 0

        var cancelable: Boolean = false

        var onDismissListener: DialogInterface.OnDismissListener? = null

        var onCancelListener: DialogInterface.OnCancelListener? = null

        var onShowListener: OnShowListener? = null
    }

    companion object {
        private const val DEF_DIM_AMOUNT = 0.3f
        private const val DEF_WIDTH_PERCENT = 0.78f
        private const val DEF_MAX_WIDTH_DP = 290f
    }
}