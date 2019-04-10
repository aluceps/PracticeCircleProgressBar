package me.aluceps.practicecountdownprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View

class CircleProgressBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progressColor = 0
    private var progressColorSecondary = 0
    private var progressStrokeWidth = 0f
    private var duration = 0

    private val progressPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = progressStrokeWidth
            color = progressColor
        }
    }

    private val progressSecondaryPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = progressStrokeWidth
            color = progressColorSecondary
        }
    }

    private val progressOval = RectF()

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

        typedArray?.getDimension(R.styleable.CircleProgressBar_progress_stroke_width, 0f)
            ?.let { progressStrokeWidth = it }

        typedArray?.getInt(R.styleable.CircleProgressBar_progress_duration, 0)
            ?.let { duration = it }

        typedArray?.recycle()
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
        canvas?.drawArc(progressOval, 270f, 250f, false, progressPaint)
    }

    private fun getColor(resId: Int): Int =
        ResourcesCompat.getColor(resources, resId, null)

    private fun Context.convertPx2Dp(px: Int): Float =
        px / context.resources.displayMetrics.density

    private fun Context.convertDp2Px(dp: Int): Float =
        dp * context.resources.displayMetrics.density
}