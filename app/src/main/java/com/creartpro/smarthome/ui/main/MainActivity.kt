package com.creartpro.smarthome.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.creartpro.smarthome.R
import com.creartpro.smarthome.ui.home.HomeActivity
import com.creartpro.smarthome.ui.onboarding.OnBoardingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var FIRST_TIME = "first_time"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)

        Handler(Looper.getMainLooper()).postDelayed({
            sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE)
            val isFirstTime = sharedPreferences.getBoolean(FIRST_TIME, true)

            if (isFirstTime) {
                val editor = sharedPreferences.edit()
                with(editor) {
                    putBoolean(FIRST_TIME, false)
                    commit()
                }

                startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
                finish()

            } else {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            }
        }, 3000)
    }
}