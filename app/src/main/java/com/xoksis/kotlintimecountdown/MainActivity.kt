package com.xoksis.kotlintimecountdown

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xoksis.kotlintimecountdown.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Variables
    private lateinit var binding: ActivityMainBinding

    private var timeSelected: Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffSet: Long = 0
    private var isStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButtonAdd.setOnClickListener {
            setTimeFunction()
        }

        binding.buttonPlayPause.setOnClickListener {
            startTimerSetup()
        }

        binding.imageButtonReset.setOnClickListener {
            resetTime()
        }

        binding.textViewAddTime.setOnClickListener {
            addExtraTime()
        }

    }

    private fun addExtraTime() {

        if (timeSelected != 0) {
            timeSelected += 15
            binding.progressBarTimer.max = timeSelected
            timePause()
            startTimer(pauseOffSet)
            Toast.makeText(this@MainActivity, "15 seconds added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime() {
        if (timeCountDown != null) {

            timeCountDown!!.cancel()
            timeProgress = 0
            timeSelected = 0
            pauseOffSet = 0
            timeCountDown = null

            binding.buttonPlayPause.text = "Start"

            isStart = true

            binding.progressBarTimer.progress = 0

            binding.textViewTimeLeft.text = "0"
        }
    }

    private fun timePause() {

        if (timeCountDown != null) {
            timeCountDown!!.cancel()
        }

    }

    private fun startTimerSetup() {

        if (timeSelected > timeProgress) {
            if (isStart) {
                binding.buttonPlayPause.text = "Pause"
                startTimer(pauseOffSet)
                isStart = false
            } else {
                isStart = true
                binding.buttonPlayPause.text = "Resume"
                timePause()
            }

        } else {
            Toast.makeText(this@MainActivity, "Enter Time", Toast.LENGTH_SHORT).show()
        }

    }

    private fun startTimer(pauseOffSetL: Long) {

        binding.progressBarTimer.progress = timeProgress

        timeCountDown =
            object : CountDownTimer((timeSelected * 1000).toLong() - pauseOffSetL * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    timeProgress++
                    pauseOffSet = timeSelected.toLong() - millisUntilFinished / 1000
                    binding.progressBarTimer.progress = timeSelected - timeProgress

                    binding.textViewTimeLeft.text = (timeSelected - timeProgress).toString()
                }

                override fun onFinish() {

                    resetTime()
                    Toast.makeText(this@MainActivity, "Time is Up", Toast.LENGTH_SHORT).show()
                }

            }.start()
    }

    private fun setTimeFunction() {

        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.add_dialog)

        val TimeDialogEditTextTimeSet = timeDialog.findViewById<EditText>(R.id.editTextGetTime)
        val TimeDialogButtonSet = timeDialog.findViewById<Button>(R.id.buttonSet)

        TimeDialogButtonSet.setOnClickListener {

            if (TimeDialogEditTextTimeSet.text.isEmpty())
                Toast.makeText(this, "Enter Time Duration", Toast.LENGTH_SHORT).show()
            else {
                resetTime()
                binding.textViewTimeLeft.text = TimeDialogEditTextTimeSet.text
                binding.buttonPlayPause.text = "Start"
                timeSelected = TimeDialogEditTextTimeSet.text.toString().toInt()
                binding.progressBarTimer.max = timeSelected

            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timeCountDown != null) {
            timeCountDown?.cancel()
            timeProgress = 0
        }
    }
}