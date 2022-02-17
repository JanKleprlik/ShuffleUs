package com.shuffleus.app.schedule


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shuffleus.app.R
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.databinding.FragmentScheduleBinding
import com.shuffleus.app.databinding.FragmentSettingsBinding
import com.shuffleus.app.settings.AddPlayerFragment
import com.shuffleus.app.utils.*

class ScheduleFragment : Fragment(), LectureCallbackListener {

    private var _binding: FragmentScheduleBinding? = null
    private val binding: FragmentScheduleBinding
        get() = _binding!!

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
        scheduleViewModel.addLecture(newLecture)
    }

    override fun onLecturesDeleted(){
        scheduleViewModel.deleteLectures()}

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

        scheduleViewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun doNothing(){}

    private fun handleLectures(lectures: List<Lecture>) {
        val adapter = LecturesAdapter(lectures)
        binding.rvAllLectures.adapter = adapter
        binding.rvAllLectures.layoutManager = LinearLayoutManager(requireContext())
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