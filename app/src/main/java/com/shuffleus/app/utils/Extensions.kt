package com.shuffleus.app.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes res:Int) =
    LayoutInflater.from(context).inflate(res, this, false)