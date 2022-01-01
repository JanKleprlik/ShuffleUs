package com.shuffleus.app.main

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuffleus.app.R
import com.shuffleus.app.data.RailsItem
import com.shuffleus.app.utils.inflate

class GroupsAdapter() : RecyclerView.Adapter<BaseRailsViewHolder<RailsItem>>(){

    var groups:List<RailsItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when(RailsItem.RailsViewType.values()[viewType]) {
            RailsItem.RailsViewType.GROUP_NAME -> RailsGroupNameViewHolder(parent.inflate(R.layout.group_name))
            RailsItem.RailsViewType.PEOPLE -> RailsPeopleViewHolder(parent.inflate(R.layout.group_item_default))
    } as BaseRailsViewHolder<RailsItem>

    override fun onBindViewHolder(holder: BaseRailsViewHolder<RailsItem>, position: Int) {
        val item = groups[position]
        holder.bind(item)
    }

    override fun getItemViewType(position: Int) = groups[position].type.ordinal

    override fun getItemCount() = groups.size
}


class RailsGroupNameViewHolder(view: View) :BaseRailsViewHolder<RailsItem.RailsGroupName>(view){
    private val groupName by lazy { view.findViewById<TextView>(R.id.txt_group_name) }

    override fun bind(item: RailsItem.RailsGroupName) {
        groupName.text = item.groupName
    }
}

class RailsPeopleViewHolder(view: View) :BaseRailsViewHolder<RailsItem.RailsPeople>(view){
    private val view = view

    private val rvPeople by lazy { view.findViewById<RecyclerView>(R.id.rv_group)}

    override fun bind(item: RailsItem.RailsPeople) {
        var adapter = UsersAdapter()

        rvPeople.adapter = adapter
        rvPeople.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapter.users = item.people
    }
}