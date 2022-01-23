package com.shuffleus.app.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.shuffleus.app.data.Group
import com.shuffleus.app.data.RailsItem
import com.shuffleus.app.databinding.FragmentMainBinding
import com.shuffleus.app.settings.SettingsActivity
import com.shuffleus.app.utils.ViewModelResponseState
import kotlinx.coroutines.flow.collect

class MainFragment: Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val adapter = GroupsAdapter()

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    /**
     * Inflate view hierarchy to this fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        /*
        mainViewModel.getGroups(4).observe(::getLifecycle) {
            when(it){
                ViewModelResponseState.Idle -> doNothing()
                ViewModelResponseState.Loading -> doNothing()
                is ViewModelResponseState.Error -> doNothing()
                is ViewModelResponseState.Success -> handleGroups(it.content)
            }
        }
        */

        mainViewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnList.setOnClickListener{
            val intent = Intent(activity, SettingsActivity::class.java)

            startActivity(intent)
        }
    }

    private fun doNothing(){}

    private fun handleGroups(groups: List<Group>){
        binding.rvGroups.adapter = adapter
        binding.rvGroups.layoutManager = LinearLayoutManager(requireContext())

        val railsItems = mutableListOf<RailsItem>()

        groups.onEach { group ->
            railsItems.add(RailsItem.RailsGroupName(group.groupName))
            railsItems.add(RailsItem.RailsPeople(group.users))
        }

        adapter.groups = railsItems
    }

    /**
     * Stop any [Glide] operations related to this fragment.
     */
    override fun onStop() {
        super.onStop()
        Glide.with(this).onStop()
    }
}