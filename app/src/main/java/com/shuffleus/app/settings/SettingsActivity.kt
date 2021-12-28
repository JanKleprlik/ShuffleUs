package com.shuffleus.app.settings

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.shuffleus.app.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.settings_fragment_container, SettingsFragment.newInstance()).commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}