package me.aluceps.practicecountdownprogressbar

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import me.aluceps.practicecountdownprogressbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private var isLongClick = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.button.setOnLongClickListener {
            Log.d("CircleProgressBar", "setOnLongClickListener")
            isLongClick = true
            binding.progress.startCountDown()
            true
        }
        binding.button.setOnTouchListener { v, e ->
            if (e.action == MotionEvent.ACTION_UP) {
                if (isLongClick) {
                    Log.d("CircleProgressBar", "setOnTouchListener: ACTION_UP")
                    binding.progress.stopCountDown()
                    isLongClick = false
                    true
                }
                false
            }
            false
        }
    }
}
