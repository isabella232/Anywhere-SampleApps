package com.medallia.anywhere_versioning_sampleapp.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.medallia.anywhere_versioning_sampleapp.R
import com.medallia.anywhere_versioning_sampleapp.ui.activities.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


const val DELAY = 1500L

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(DELAY)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }
}
