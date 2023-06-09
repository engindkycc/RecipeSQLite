package com.engindkyc.recipesqlite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.engindkyc.recipesqlite.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

        private lateinit var binding : ActivitySplashScreenBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

                val timer = object: CountDownTimer(6000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}

                        override fun onFinish() {

                                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                        }
                }

                timer.start()

    }

}