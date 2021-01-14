package com.example.home_task_l13

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.home_task_l13.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var hand: Handler

    companion object {
        var COUNTER = 0
        const val STATUS_IN_PROGRESS = 1
        const val STATUS_NOT_IN_PROGRESS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBinding()
        setupListeners()
        handleHelper()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.btnProgress.setOnClickListener {
            someTask()
        }
    }

    private fun handleHelper() {
        hand = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    STATUS_IN_PROGRESS -> {
                        btnProgress.visibility = View.GONE
                        tvText.visibility = View.GONE
                        progressCircular.visibility = View.VISIBLE
                    }
                    STATUS_NOT_IN_PROGRESS -> {
                        btnProgress.visibility = View.VISIBLE
                        tvText.visibility = View.VISIBLE
                        progressCircular.visibility = View.GONE
                    }
                }
            }
        }
        hand.sendEmptyMessage(STATUS_NOT_IN_PROGRESS)
    }

    private fun someTask() {
        tvText.visibility = View.GONE
        progressCircular.visibility = View.VISIBLE
        val counterForTime = COUNTER.toFloat()
        val counterSecond = ((counterForTime + 1.0f)/10)*1000
        val t = Thread(Runnable {
            try {
                hand.sendEmptyMessage(STATUS_IN_PROGRESS)
                TimeUnit.MILLISECONDS.sleep(counterSecond.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                hand.sendEmptyMessage(STATUS_NOT_IN_PROGRESS)
            }
        })
        t.start()
        COUNTER += 1
        tvText.text = COUNTER.toString()
    }
}

