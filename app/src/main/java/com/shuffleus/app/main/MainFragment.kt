package com.shuffleus.app.main

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat.*
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.shuffleus.app.App
import com.shuffleus.app.R
import com.shuffleus.app.data.Group
import com.shuffleus.app.data.RailsItem
import com.shuffleus.app.databinding.FragmentMainBinding
import com.shuffleus.app.settings.SettingsActivity
import com.shuffleus.app.utils.ViewModelResponseState
import kotlinx.coroutines.launch

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

        binding.btnShuffle.setOnClickListener{
            lifecycleScope.launch {
                val time = mainViewModel.getTimeInMillis().toLong()
                doShuffle(time)
            }
        }

        binding.progressTimer.max = 100
        binding.progressTimer.hide()
    }

    private fun doNothing(){}

    private suspend fun doShuffle(time:Long){
        mainViewModel.getGroups().observe(::getLifecycle) {
            when(it){
                ViewModelResponseState.Idle -> doNothing()
                ViewModelResponseState.Loading -> doNothing()
                is ViewModelResponseState.Error -> doNothing()
                is ViewModelResponseState.Success -> handleGroups(it.content)
            }
        }


        val timer = object: CountDownTimer(time, time/110) { //update hundredth of time
            override fun onTick(millisUntilFinished: Long) {
                //TODO: update loading bar perhaps
                binding.progressTimer.incrementProgressBy(1)
            }

            override fun onFinish() {
                // send notification
                //fails on double invokation after changing activity

                binding.progressTimer.hide()
                Toast.makeText(context, "Time to shuffle!", Toast.LENGTH_LONG).show()
                fireNotification()
            }
        }

        binding.progressTimer.progress = 0
        binding.progressTimer.show()
        timer.start()

    }

    private fun fireNotification() {
        val notificationIntent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notificationPendingIntent = TaskStackBuilder.create(requireContext()).run {
            addNextIntentWithParentStack(notificationIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = Builder(requireContext(), App.NOTIFICATION_CHANNEL_MAIN_ID)
            .setSmallIcon(R.drawable.ic_baseline_add_24)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.timer_notification))
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, notification)
        }
    }

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