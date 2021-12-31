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

/*
class GroupsAdapter(groups: List<Group>): RecyclerView.Adapter<GroupViewHolder>(){

    var groups = groups
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item_default, parent, false)

        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int {
        return groups.count()
    }
}

class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    var rvGroups: RecyclerView = view.findViewById(R.id.rv_group)

    fun bind(group: Group){
        val adapter = UsersAdapter(group.users)




        rvGroups
    }

}
*/

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
    val groupName by lazy { view.findViewById<TextView>(R.id.txt_group_name) }

    override fun bind(item: RailsItem.RailsGroupName) {
        groupName.text = item.groupName
    }
}

class RailsPeopleViewHolder(view: View) :BaseRailsViewHolder<RailsItem.RailsPeople>(view){
    val view = view

    val rvPeople by lazy { view.findViewById<RecyclerView>(R.id.rv_group)}

    override fun bind(item: RailsItem.RailsPeople) {
        var adapter = UsersAdapter()

        rvPeople.adapter = adapter
        rvPeople.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, true)
        adapter.users = item.people
    }
}