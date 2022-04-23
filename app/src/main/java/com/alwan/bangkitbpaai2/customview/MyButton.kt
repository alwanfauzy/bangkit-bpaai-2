package com.alwan.bangkitbpaai2.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.alwan.bangkitbpaai2.R

class MyButton : AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0
    private var isOutlined = false

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs, defStyleAttr)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        text = resources.getString(if (isOutlined) R.string.register else R.string.login)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int = 0) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MyButton, defStyleAttr, 0)

        isOutlined = a.getBoolean(R.styleable.MyEditText_email, false)

        txtColor = ContextCompat.getColor(
            context,
            if (isOutlined) R.color.teal_500 else android.R.color.background_light
        )
        enabledBackground =
            ContextCompat.getDrawable(
                context,
                if (isOutlined) R.drawable.bg_button_teal_outlined else R.drawable.bg_button_teal
            ) as Drawable
        disabledBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_button_teal_disabled) as Drawable

        a.recycle()
    }
}