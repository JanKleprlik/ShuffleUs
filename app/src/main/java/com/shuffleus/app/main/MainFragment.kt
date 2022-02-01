package com.shuffleus.app.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.shuffleus.app.data.Group
import com.shuffleus.app.data.RailsItem
import com.shuffleus.app.databinding.FragmentMainBinding
import com.shuffleus.app.settings.SettingsActivity
import com.shuffleus.app.utils.Constants
import com.shuffleus.app.utils.PrefUtil
import com.shuffleus.app.utils.TimerExpiredReceiver
import com.shuffleus.app.utils.ViewModelResponseState
import kotlinx.coroutines.launch

class MainFragment: Fragment() {

    // GENERAL
    private val mainViewModel by viewModels<MainViewModel>()
    private val adapter = GroupsAdapter()

    // MVVM
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    // TIMER
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var secondsRemaining: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.loadData()
    }

    override fun onResume() {
        super.onResume()

        // update timer if necessary
        initTimer()
        removeAlarm(this.context!!)
    }

    override fun onPause() {
        super.onPause()

        // cancel timer and set an alarm
        if (timerState == TimerState.Running){
            timer.cancel()
            setAlarm(this.context!!, nowSeconds, secondsRemaining)
        }

        // update times
        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this.context!!)
        PrefUtil.setSecondsRemaining(secondsRemaining, this.context!!)
        PrefUtil.setTimerState(timerState, this.context!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnList.setOnClickListener{
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btnShuffle.setOnClickListener{
            lifecycleScope.launch {
                val time = mainViewModel.getTimeInMillis().toLong()
                val timeInMinutes = (time / (1000 * 60)).toInt()
                PrefUtil.setTimerLength(timeInMinutes, context!!)
                doShuffle()
            }
        }
    }

    /**
     * Stop any [Glide] operations related to this fragment.
     */
    override fun onStop() {
        super.onStop()
        Glide.with(this).onStop()
    }
    // endregion

    private fun doNothing(){}

    private suspend fun doShuffle(){
        mainViewModel.getGroups().observe(::getLifecycle) {
            when(it){
                ViewModelResponseState.Idle -> doNothing()
                ViewModelResponseState.Loading -> doNothing()
                is ViewModelResponseState.Error -> doNothing()
                is ViewModelResponseState.Success -> handleGroups(it.content)
            }
        }
        // Cancel ongoing timer
        if (timerState == TimerState.Running)
        {
            timer.cancel()
            timerState = TimerState.Stopped
            removeAlarm(this.context!!)
            onTimerFinished()
        }
        // Set new timer
        else
        {
            startTimer()
            timerState = TimerState.Running
        }
    }

    // endregion

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

    // region TIMER OPERATIONS

    enum class TimerState{
        Stopped, Paused, Running
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this.context!!)

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this.context!!)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this.context!!)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()

        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        setNewTimerLength()

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this.context!!)
        secondsRemaining = timerLengthSeconds

        updateCountdownUI()
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this.context!!)
        timerLengthSeconds = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this.context!!)
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.txtTime.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
    }


    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            intent.action = Constants.ACTION_START
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = System.currentTimeMillis() / 1000
    }

    // endregion
}