package com.example.customviewtemperatureeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class TemperatureEditorView(
    context: Context,
    private val attributeSet: AttributeSet?,
    private val defStyleAttr: Int,
    private val defStyleRes: Int
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : this(context, attributeSet, defStyleAttr, R.style.BlueTemperatureEditorStyle)

    constructor(context: Context, attributeSet: AttributeSet?)
            : this(context, attributeSet, R.attr.temperatureEditorStyle)

    constructor(context: Context)
            : this(context, null)

    private lateinit var paintBackground: Paint
    private lateinit var paintBackgroundCenter: Paint
    private lateinit var paintProgress: Paint
    private lateinit var paintSlider: Paint

    private var outerRadius = 0f
    private var progressRadius = 0f
    private var innerRadius = 0f
    private var centerX = 0f
    private var centerY = 0f

    private var widthSlider = 1.7f
    private var heightSlider = 60f
    private var widthProgress = 25f

    private lateinit var reactProgress: RectF

    private var colorBackgroundCircle: Int = DEFAULT_COLOR_BACKGROUND
    private var colorBackgroundCenterStart = DEFAULT_COLOR_BACKGROUND_CENTRE_START
    private var colorBackgroundCenterEnd = DEFAULT_COLOR_BACKGROUND_CENTRE_END
    private var colorProgressStart = DEFAULT_COLOR_PROGRESS_START
    private var colorProgressEnd = DEFAULT_COLOR_PROGRESS_END
    private var colorSlider = DEFAULT_COLOR_SLIDER

    private var progress = 0f

    @SuppressLint("ResourceAsColor")
    private fun initPaint() {
        paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = colorBackgroundCircle
        }
        initPaintProgress()
        initPaintBackgroundCenter()
        initPaintSlider()
    }

    private fun initPaintProgress() {
        paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = widthProgress
            strokeCap = Paint.Cap.ROUND

            val matrix = Matrix().apply {
                setRotate(45F, centerX, centerY)
            }

            val gradient = SweepGradient(
                centerX, centerY,
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

        val matrix = Matrix().apply { setRotate(-115f, centerX, centerY) }

        val gradient = SweepGradient(
            centerX, centerY,
            IntArray(3).apply {
                this[0] = colorBackgroundCenterStart
                this[1] = colorBackgroundCenterEnd
                this[2] = colorBackgroundCenterStart
            },
            null
        ).apply { setLocalMatrix(matrix) }

        paintBackgroundCenter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = gradient
        }
    }

    private fun initPaintSlider() {
        paintSlider = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeCap = Paint.Cap.ROUND
            color = colorSlider
            style = Paint.Style.STROKE
            strokeWidth = heightSlider
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        progress++
        canvas!!.drawCircle(centerX, centerY, progressRadius, paintBackground)
        canvas.drawCircle(centerX, centerY, innerRadius, paintBackgroundCenter)
        canvas.drawArc(reactProgress, 0f, progress % 360, false, paintProgress)
        canvas.drawArc(reactProgress, progress % 360, widthSlider, false, paintSlider)

        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        outerRadius = (w - paddingLeft - paddingRight) / 2f

        widthProgress = outerRadius / 10f
        heightSlider = widthProgress * 1.3f
        heightSlider = widthProgress * 1.3f

        val widthOuterBackground = outerRadius / 2f


        progressRadius = outerRadius - widthOuterBackground
        innerRadius = progressRadius - widthProgress

        centerX = paddingLeft + outerRadius
        centerY = paddingTop + outerRadius


        reactProgress = RectF(
            centerX - progressRadius + widthProgress,
            centerY - progressRadius + widthProgress,
            centerX + progressRadius - widthProgress,
            centerY + progressRadius - widthProgress
        )

        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleRes)
        }

        initPaint()
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

        colorSlider = typedArray.getColor(
            R.styleable.temperatureEditorView_background_slider,
            DEFAULT_COLOR_SLIDER
        )

        typedArray.recycle()
    }

    companion object {
        const val DEFAULT_COLOR_BACKGROUND = Color.GRAY
        const val DEFAULT_COLOR_BACKGROUND_CENTRE_START = Color.BLACK
        const val DEFAULT_COLOR_BACKGROUND_CENTRE_END = Color.GRAY
        const val DEFAULT_COLOR_PROGRESS_START = Color.BLUE
        const val DEFAULT_COLOR_PROGRESS_END = Color.CYAN
        const val DEFAULT_COLOR_SLIDER = Color.BLACK
    }

}