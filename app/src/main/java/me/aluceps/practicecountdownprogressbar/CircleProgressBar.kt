package me.aluceps.practicecountdownprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import java.util.*

class CircleProgressBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progressColor = 0
    private var progressColorSecondary = 0
    private var progressColorBase = 0
    private var progressStrokeWidth = 0f
    private var duration = 0
    private var roundStyle = false
    private var isIncrement = false

    private val progressPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = progressStrokeWidth
            color = progressColor
            if (roundStyle) {
                strokeJoin = Paint.Join.ROUND
                strokeCap = Paint.Cap.ROUND
            }
        }
    }

    private val progressSecondaryPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = progressStrokeWidth
            color = progressColorBase
        }
    }

    private val progressOval = RectF()

    private var started = false
    private var startTime = 0L

    private val incrementMax by lazy { duration / 1000 }
    private var currentValue = 0f
    private var animation: ValueAnimator? = null

    interface ProgressState {
        fun onStarted()
        fun onFinished()
        fun onProgress(progress: Int)
    }

    private var listener: ProgressState? = null

    fun setOnProgressState(listener: ProgressState) {
        this.listener = listener
    }

    init {
        setup(context, attrs, defStyleAttr)
    }

    private fun setup(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0)

        typedArray?.getColor(
            R.styleable.CircleProgressBar_progress_color,
            getColor(R.color.colorProgress)
        )?.let { progressColor = it }

        typedArray?.getColor(
            R.styleable.CircleProgressBar_progress_secondary_color,
            getColor(R.color.colorProgressSecondaly)
        )?.let { progressColorSecondary = it }

        typedArray?.getColor(
            R.styleable.CircleProgressBar_progress_base_color,
            getColor(R.color.colorProgressBase)
        )?.let { progressColorBase = it }

        typedArray?.getDimension(R.styleable.CircleProgressBar_progress_stroke_width, 0f)
            ?.let { progressStrokeWidth = it }

        typedArray?.getInt(R.styleable.CircleProgressBar_progress_duration, 0)
            ?.let { duration = it * 1000 }

        typedArray?.getBoolean(R.styleable.CircleProgressBar_progress_round_style, false)
            ?.let { roundStyle = it }

        typedArray?.getBoolean(R.styleable.CircleProgressBar_progress_is_increment, false)
            ?.let { isIncrement = it }

        typedArray?.recycle()

        Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    post { invalidate() }
                }
            }, 10, 10)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        progressOval.set(
            progressStrokeWidth,
            progressStrokeWidth,
            width - progressStrokeWidth,
            height - progressStrokeWidth
        )
        canvas?.drawArc(progressOval, 0f, 360f, false, progressSecondaryPaint)
        if (isIncrement) {
            (animation?.animatedValue as? Float)?.let { angle ->
                canvas?.drawArc(progressOval, 270f, angle, false, progressPaint)
            }
        } else {
            if (started) getAngle().let { angle ->
                canvas?.drawArc(progressOval, 270f, angle, false, progressPaint)
                listener?.onProgress((angle / 360 * 100).toInt())
            } else {
                canvas?.drawArc(progressOval, 270f, 360f, false, progressPaint)
            }
        }
    }

    private fun getAngle(): Float {
        val currentAngle = (Date().time - startTime).toFloat() / duration * 360f
        return when {
            startTime == 0L -> 0f
            currentAngle > 360f -> {
                stopCountDown()
                0f
            }
            else -> currentAngle
        }
    }

    fun startCountDown() {
        if (isIncrement) return
        started = true
        listener?.onStarted()
        startTime = Date().time
    }

    fun stopCountDown() {
        if (isIncrement) return
        if (!started) return
        started = false
        listener?.onFinished()
        startTime = 0
    }

    fun increment() {
        if (!isIncrement) return
        if (currentValue < incrementMax) {
            currentValue++
            start()
        }
    }

    fun reset() {
        if (!isIncrement) return
        currentValue = 0f
    }

    fun start() {
        if (!isIncrement) return
        val toValue = currentValue / incrementMax * 360
        animation = ValueAnimator.ofFloat(0f, toValue)
        animation?.duration = 500
        animation?.start()
    }

    fun setProgressColorPrimary() {
        progressPaint.color = progressColor
    }

    fun setProgressColorSecondary() {
        progressPaint.color = progressColorSecondary
    }

    private fun getColor(resId: Int): Int =
        ResourcesCompat.getColor(resources, resId, null)
}