package me.aluceps.practicecountdownprogressbar

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import me.aluceps.practicecountdownprogressbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CircleProgressBar.ProgressState {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private var isLongClick = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progress.setOnProgressState(this)
        binding.button.setOnClickListener {
            Log.d("###", "setOnClickListener")
        }
        binding.button.setOnLongClickListener {
            Log.d("###", "setOnLongClickListener")
            isLongClick = true
            binding.progress.startCountDown()
            true
        }
        binding.button.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_UP -> if (!isLongClick) false else {
                    Log.d("###", "setOnTouchListener: ACTION_UP")
                    binding.progress.stopCountDown()
                    isLongClick = false
                    true
                }
                else -> false
            }
        }
    }

    override fun onStarted() {
        Log.d("CircleProgressBar", "progress: start")
    }

    override fun onFinished() {
        Log.d("CircleProgressBar", "progress: finish")
    }

    override fun onProgress(progress: Int) {
        Log.d("CircleProgressBar", "progress: $progress")
    }
}
