package com.shuffleus.app.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shuffleus.app.data.RailsItem

abstract class BaseRailsViewHolder<T: RailsItem>(view: View): RecyclerView.ViewHolder(view) {

    abstract fun bind(item: T)
}