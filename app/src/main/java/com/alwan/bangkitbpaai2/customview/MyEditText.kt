package com.alwan.bangkitbpaai2.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.alwan.bangkitbpaai2.R
import com.alwan.bangkitbpaai2.util.isValidEmail

class MyEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var clearButtonImage: Drawable
    private lateinit var emailImage: Drawable
    private lateinit var passwordImage: Drawable
    private lateinit var enabledBackground: Drawable
    private var isEmail: Boolean = false
    private var isPassword: Boolean = false

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
        setPadding(32, 32, 32, 32)
        background = enabledBackground
        gravity = Gravity.CENTER_VERTICAL
        compoundDrawablePadding = 16
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int = 0) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MyEditText, defStyleAttr, 0)

        isEmail = a.getBoolean(R.styleable.MyEditText_email, false)
        isPassword = a.getBoolean(R.styleable.MyEditText_password, false)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
        emailImage = ContextCompat.getDrawable(context, R.drawable.ic_mail) as Drawable
        passwordImage = ContextCompat.getDrawable(context, R.drawable.ic_key) as Drawable

        if (isEmail) {
            setButtonDrawables(startOfTheText = emailImage)
        } else if (isPassword) {
            setButtonDrawables(startOfTheText = passwordImage)
        }

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = s.toString()
                val emailError = resources.getString(R.string.invalid_email)
                val passwordError = resources.getString(R.string.invalid_password)

                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
                error =
                    if (isPassword && input.length < 6 && input.isNotEmpty()) {
                        passwordError
                    } else if (isEmail && !input.isValidEmail()) {
                        emailError
                    } else {
                        null
                    }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

        a.recycle()
    }

    private fun showClearButton() {
        when {
            isEmail -> setButtonDrawables(
                startOfTheText = emailImage,
                endOfTheText = clearButtonImage
            )
            isPassword -> setButtonDrawables(
                startOfTheText = passwordImage,
                endOfTheText = clearButtonImage
            )
            else -> setButtonDrawables(endOfTheText = clearButtonImage)
        }
    }

    private fun hideClearButton() {
        when {
            isEmail -> setButtonDrawables(startOfTheText = emailImage)
            isPassword -> setButtonDrawables(startOfTheText = passwordImage)
            else -> setButtonDrawables()
        }
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}

