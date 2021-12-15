package me.aluceps.practicecountdownprogressbar

import android.annotation.SuppressLint
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import me.aluceps.practicecountdownprogressbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CircleProgressBar.ProgressState {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private var isLongClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progress.setOnProgressState(this)
        setupListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        binding.button.setOnClickListener {
            Log.d("###", "setOnClickListener")
        }
        binding.button.setOnLongClickListener {
            Log.d("###", "setOnLongClickListener")
            isLongClick = true
            binding.progress.startCountDown()
            true
        }
        binding.button.setOnTouchListener { _, e ->
            when (e.action) {
                MotionEvent.ACTION_UP -> if (!isLongClick) false else {
                    Log.d("###", "setOnTouchListener: ACTION_UP")
                    isLongClick = false
                    binding.progress.stopCountDown()
                    binding.progress.setProgressColorPrimary()
                    true
                }
                else -> false
            }
        }
        binding.buttonIncrement.setOnClickListener {
            binding.progressIncrement.increment()
        }
        binding.buttonReset.setOnClickListener {
            binding.progressIncrement.reset()
        }
    }

    override fun onStarted() {
        Log.d("CircleProgressBar", "progress: start")
        binding.button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_down_button))
        binding.progress.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up_progress))
    }

    override fun onFinished() {
        Log.d("CircleProgressBar", "progress: finish")
        binding.button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up_button))
        binding.progress.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_down_progress))
    }

    override fun onProgress(progress: Float) {
        Log.d("CircleProgressBar", "progress: $progress")
        if (progress > 50) {
            binding.progress.setProgressColorPrimary()
        } else {
            binding.progress.setProgressColorSecondary()
        }
    }
}
