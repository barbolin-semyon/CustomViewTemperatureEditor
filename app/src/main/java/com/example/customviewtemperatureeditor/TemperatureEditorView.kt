package com.example.customviewtemperatureeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

class TemperatureEditorView(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : this(context, attributeSet, defStyleAttr, R.style.BlueTemperatureEditorStyle)

    constructor(context: Context, attributeSet: AttributeSet?)
            : this(context, attributeSet, R.attr.temperatureEditorStyle)

    constructor(context: Context)
            : this(context, null)


    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleRes)
        }
        initPaint()
    }

    private lateinit var paintBackground: Paint
    private lateinit var paintBackgroundCenter: Paint
    private lateinit var paintProgress: Paint

    private var colorBackgroundCircle: Int = DEFAULT_COLOR_BACKGROUND
    private var colorBackgroundCenterStart = DEFAULT_COLOR_BACKGROUND_CENTRE_START
    private var colorBackgroundCenterEnd = DEFAULT_COLOR_BACKGROUND_CENTRE_END
    private var colorProgressStart = DEFAULT_COLOR_PROGRESS_START
    private var colorProgressEnd = DEFAULT_COLOR_PROGRESS_END

    @SuppressLint("ResourceAsColor")
    private fun initPaint() {
        paintBackground = Paint().apply {
            color = colorBackgroundCircle
        }
        initPaintProgress()
        initPaintBackgroundCenter()
    }

    private fun initPaintProgress() {
        paintProgress = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 25F
            isAntiAlias = true

            val matrix = Matrix().apply {
                setRotate(45F, 500F, 500F)
            }

            val gradient = SweepGradient(
                500f, 500f,
                IntArray(3).apply {
                    this[0] = colorProgressStart
                    this[1] = colorProgressEnd
                    this[2] = colorProgressStart
                },
                null
            )
            gradient.setLocalMatrix(matrix)
            shader = gradient
        }
    }

    private fun initPaintBackgroundCenter() {

        val matrix = Matrix().apply { setRotate(-115f, 500f, 500f) }

        val gradient = SweepGradient(
            500f, 500f,
            IntArray(3).apply {
                this[0] = colorBackgroundCenterStart
                this[1] = colorBackgroundCenterEnd
                this[2] = colorBackgroundCenterStart
            },
            null
        ).apply { setLocalMatrix(matrix) }

        paintBackgroundCenter = Paint().apply {
            shader = gradient
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawCircle(500f, 500f, 240f, paintBackground)
        canvas.drawCircle(500f, 500f, 187f, paintBackgroundCenter)
        canvas.drawArc(RectF(300f, 300f, 700f, 700f), 0f, 270f, false, paintProgress)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttributes(attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.temperatureEditorView,
            defStyleAttr, defStyleRes
        )

        colorBackgroundCircle = typedArray.getColor(
            R.styleable.temperatureEditorView_background_circle,
            DEFAULT_COLOR_BACKGROUND
        )

        colorBackgroundCenterStart = typedArray.getColor(
            R.styleable.temperatureEditorView_background_centre_start,
            DEFAULT_COLOR_BACKGROUND_CENTRE_START
        )
        colorBackgroundCenterEnd = typedArray.getColor(
            R.styleable.temperatureEditorView_background_centre_end,
            DEFAULT_COLOR_BACKGROUND_CENTRE_END
        )
        colorProgressStart = typedArray.getColor(
            R.styleable.temperatureEditorView_background_progress_start,
            DEFAULT_COLOR_PROGRESS_START
        )
        colorProgressEnd = typedArray.getColor(
            R.styleable.temperatureEditorView_background_progress_end,
            DEFAULT_COLOR_PROGRESS_END
        )


        typedArray.recycle()
    }

    companion object {
        const val DEFAULT_COLOR_BACKGROUND = Color.GRAY
        const val DEFAULT_COLOR_BACKGROUND_CENTRE_START = Color.BLACK
        const val DEFAULT_COLOR_BACKGROUND_CENTRE_END = Color.GRAY
        const val DEFAULT_COLOR_PROGRESS_START = Color.BLUE
        const val DEFAULT_COLOR_PROGRESS_END = Color.CYAN
    }

}