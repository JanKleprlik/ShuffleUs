package com.shuffleus.app.schedule


import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shuffleus.app.R
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.databinding.FragmentScheduleBinding
import com.shuffleus.app.utils.*
import org.w3c.dom.Text
import android.view.WindowManager
import androidx.core.view.allViews
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class ScheduleFragment : Fragment(), LectureCallbackListener {
    private var _binding: FragmentScheduleBinding? = null
    private val binding: FragmentScheduleBinding
        get() = _binding!!

    private var width: Int = 0
    private var height: Int = 0
    private val menuWidthPart : Int = 20
    private val menuHeightPart : Int = 20
    private var firstLectureStart : Int = 24*60
    private var lastLectureEnd : Int = 0*60

    private val scheduleViewModel by viewModels<ScheduleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        binding.fabAdd.setOnClickListener{
            AddLectureFragment(this).show(childFragmentManager,"addLectureDialog")
        }

        return binding.root
    }

    override fun onLectureAdded(newLecture: Lecture){
        lifecycleScope.launch{
            scheduleViewModel.addLecture(newLecture)
        }
    }

    override fun onLecturesDeleted(){
        binding.rlLectures.removeAllViewsInLayout()
        lifecycleScope.launchWhenCreated {
            scheduleViewModel.deleteLectures()}
        }

    override fun onStart() {
        super.onStart()

        scheduleViewModel.getLectures().observe(viewLifecycleOwner){
            when(it){
                ViewModelResponseState.Idle -> doNothing()
                ViewModelResponseState.Loading -> doNothing()
                is ViewModelResponseState.Error -> doNothing()
                is ViewModelResponseState.Success -> handleLectures(it.content)
            }
        }
        lifecycleScope.launch {
            scheduleViewModel.loadData()
        }
    }

    // This function is called upon retriving information about lectures
    private fun handleLectures(lectures: List<Lecture>) {
        binding.rlLectures.removeAllViewsInLayout()
        getAvailableWidthAndHeight()
        buildSchedule(lectures)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun doNothing(){}

    // This function builds headers and draws lectures.
    private fun buildSchedule(lectures: List<Lecture>){
        setLecturesTimeRange(lectures)
        val difference = lastLectureEnd - firstLectureStart

        buildDaysHeader()
        buildHoursHeader(difference)

        for (item in lectures) {
            binding.rlLectures.addView(buildLectureView(item))
        }
    }

    // This function is used for computing difference between start of first
    // lecture and end of last lecture
    private fun setLecturesTimeRange(lectures: List<Lecture>){
        for(item in lectures){
            if(item.startTime < firstLectureStart){
                firstLectureStart = item.startTime
            }
            if(item.endTime > lastLectureEnd){
                lastLectureEnd = item.endTime
            }
        }

        if(lastLectureEnd - firstLectureStart < 3*90+20){
            when {
                firstLectureStart <= 9*60 -> {
                    lastLectureEnd += 2*90+20
                }
                lastLectureEnd >= 17*60+20 -> {
                    firstLectureStart -= 2*90+20
                }
                else -> {
                    lastLectureEnd += 90+10
                    firstLectureStart -= 90+10
                }
            }
        }
    }

    //Builds header for each day
    private fun buildDaysHeader(){
        val days = arrayOf("Po","Út","St","Čt","Pá")
        var i = 0
        for(day in days) {
            val current = TextView(this.context)
            current.text = day

            val start = height*0.95*i/5
            val offset = height/menuHeightPart

            current.x = 0.toFloat()
            current.y = (start+offset).toFloat()
            current.layoutParams = RelativeLayout.LayoutParams(width/menuWidthPart, height/5)
            binding.rlSchedule.addView(current)
            i += 1
        }
    }

    //Builds hours header by adding child view to rlLectures.
    private fun buildHoursHeader(difference: Int){
        var current = firstLectureStart
        while(current < firstLectureStart + difference){
            val hours = (current/60).toString()
            var minutes = (current%60).toString()
            minutes = formatMinutes(minutes)

            val inflater = LayoutInflater.from(this.context)
            val timeView = inflater.inflate(R.layout.time_item, null, false)

            timeView.findViewById<TextView>(R.id.tvTime).text =hours.plus(":").plus(minutes)

            timeView.x = timeToXCoordinate(current)
            timeView.y = 0.toFloat()
            binding.rlLectures.addView(timeView)
            current += 100
        }
    }

    // Gets information about available dimensions.
    private fun getAvailableWidthAndHeight(){
        val displayMetrics = DisplayMetrics()

        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        height = (height-getStatusBarHeight())
    }

    // We need to know status bar height for correct computation of available space
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    // Used for building time header - formats to MM
    private fun formatMinutes(time:String):String
    {
        return if(time.length == 1){
            0.toString().plus(time)
        } else{
            time
        }
    }

    // For given time determines determines x offset
    private fun timeToXCoordinate(time: Int): Float {
        val availableWidth: Float = (width * 0.95).toFloat()
        val widthPerMinute = availableWidth / (lastLectureEnd - firstLectureStart)
        val xWithoutOffset = widthPerMinute * (time - firstLectureStart)
        return xWithoutOffset + width / menuWidthPart
    }

    // Given information about lecture builds and returns
    // view consisting of information about the lecture
    private fun buildLectureView(item: Lecture) :View{
        val inflater = LayoutInflater.from(this.context)
        val lec = inflater.inflate(R.layout.lecture_item, null, false)

        lec.findViewById<TextView>(R.id.tvLectureLecturer).text = item.lecturer
        lec.findViewById<TextView>(R.id.tvLectureCode).text = item.code
        lec.findViewById<TextView>(R.id.tvLectureRoom).text = item.room
        lec.findViewById<TextView>(R.id.tvLectureSubject).text = item.subject

        val hours = (item.startTime/60).toString()
        var minutes = (item.startTime%60).toString()
        minutes = formatMinutes(minutes)
        lec.findViewById<TextView>(R.id.tvLectureTime).text = hours.plus(":").plus(minutes)

        lec.x = timeToXCoordinate(item.startTime)
        lec.y = (height*0.95/5*item.day+height/20).toFloat()

        val lectureEndX = timeToXCoordinate(item.endTime)
        lec.layoutParams = RelativeLayout.LayoutParams((lectureEndX -  lec.x).toInt(), height/5*95/100)

        if(item.lection){
            lec.setBackgroundResource(R.drawable.dark_blue_with_border)
        }
        else{
            lec.setBackgroundResource(R.drawable.light_blue_with_border)
        }
        return lec
    }

    companion object {
        /**
         * Factory method to create fragment instance. Framework requires empty default constructor.
         */
        @JvmStatic
        fun newInstance(): ScheduleFragment {
            return ScheduleFragment()
        }
    }
}