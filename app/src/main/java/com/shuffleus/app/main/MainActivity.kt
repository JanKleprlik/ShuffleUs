package com.shuffleus.app.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shuffleus.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, MainFragment.newInstance()).commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}