package com.shuffleus.app.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shuffleus.app.R

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        supportFragmentManager.beginTransaction().add(R.id.schedule_fragment_container, ScheduleFragment.newInstance()).commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}