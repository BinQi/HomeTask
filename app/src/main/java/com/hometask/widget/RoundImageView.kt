package com.hometask.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import com.hometask.app.R

/**
 * 
 *
 * 可设置圆角的ImageView
 */
class RoundImageView : AppCompatImageView {

    private var normalOutlineProvider: ViewOutlineProvider? = null
    private var innerOutlineProvider: ViewOutlineProvider? = null

    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        initData(attrs)
    }

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(ctx, attrs, defStyleAttr) {
        initData(attrs)
    }

    private fun initData(attrs: AttributeSet?) {
        if (attrs != null) {
            var typedArray: TypedArray? = null
            val radius: Float
            val innerRadius: Float
            try {
                typedArray =
                    context.theme.obtainStyledAttributes(attrs, R.styleable.RoundImageView, 0, 0)

                radius =
                    typedArray.getDimensionPixelOffset(R.styleable.RoundImageView_radius, -1)
                        .toFloat()

                innerRadius =
                    typedArray.getDimensionPixelOffset(R.styleable.RoundImageView_innerRadius, -1)
                        .toFloat()
            } finally {
                typedArray?.recycle()
            }

            if (radius > -1 || innerRadius > -1) {
                setRoundRadius(radius, innerRadius)
            }
        }
    }

    /**
     * 设置圆角大小
     *
     * @param radius 正常状态的圆角值(px)
     * @param innerRadius 内矩形状态的圆角值(px)，< 0 则不设置，默认使用[radius]的值
     */
    fun setRoundRadius(radius: Float, innerRadius: Float = -1f) {
        if (radius >= 0) {
            normalOutlineProvider = createViewOutlineProvider(radius)
        }
        if (innerRadius >= 0) {
            innerOutlineProvider = createViewOutlineProvider(innerRadius)
        }
        updateUI()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        updateUI()
    }

    private fun updateUI() {
        var provider = if (isSelected) innerOutlineProvider else normalOutlineProvider
        if (provider == null) {
            provider = normalOutlineProvider ?: innerOutlineProvider
        }
        outlineProvider = provider
        clipToOutline = true
    }

    companion object {

        private fun createViewOutlineProvider(radius: Float): ViewOutlineProvider {
            return object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, radius)
                }
            }
        }
    }
}