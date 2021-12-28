package com.shuffleus.app.data

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true )
data class User(val name: String,
                val surname: String,
                val isActive: Boolean,
                )