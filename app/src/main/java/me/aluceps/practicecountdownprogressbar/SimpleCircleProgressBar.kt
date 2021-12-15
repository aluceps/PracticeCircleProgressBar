package me.aluceps.practicecountdownprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.util.Timer
import java.util.TimerTask

class SimpleCircleProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val MAX_ANGLE = 360f
        private const val START_ANGLE = 270f
    }

    private var progressColor = 0
    private var progressBackgroundColor = 0
    private var progressStrokeWidth = 0f

    private val progressPaint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

    private val progressBackgroundPaint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

    private val progressOval = RectF()
    private var angle = 0f

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.SimpleCircleProgressBar,
            defStyleAttr,
            0
        )
        progressColor =
            typedArray.getColor(
                R.styleable.SimpleCircleProgressBar_simple_progress_color,
                ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
            )
        progressBackgroundColor =
            typedArray.getColor(
                R.styleable.SimpleCircleProgressBar_simple_progress_background_color,
                ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
            )
        progressStrokeWidth =
            typedArray.getDimension(
                R.styleable.SimpleCircleProgressBar_simple_progress_stroke_width,
                4f
            )
        typedArray.recycle()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                post { invalidate() }
            }
        }, 10, 10)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        progressPaint.apply {
            color = progressColor
            strokeWidth = progressStrokeWidth
        }
        progressBackgroundPaint.apply {
            color = progressBackgroundColor
            strokeWidth = progressStrokeWidth
        }
        progressOval.set(
            progressStrokeWidth,
            progressStrokeWidth,
            width - progressStrokeWidth,
            height - progressStrokeWidth
        )
        canvas.drawArc(
            progressOval,
            0f,
            MAX_ANGLE,
            false,
            progressBackgroundPaint
        )
        canvas.drawArc(
            progressOval,
            START_ANGLE,
            angle,
            false,
            progressPaint
        )
    }

    fun setProgress(progress: Float) {
        when {
            progress < 0f -> 0f
            progress > 1f -> 1f
            else -> progress
        }.let {
            angle = it * MAX_ANGLE
        }
    }
}
