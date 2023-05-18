package com.engindkyc.recipesqlite

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.engindkyc.recipesqlite.databinding.ActivitySplashScreenBinding

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
        private val splashScreen = 10000
        lateinit var binding : ActivitySplashScreenBinding
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


                //Splash Screen create
                /*Handler().postDelayed({

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                },splashScreen.toLong())*/

    }
}