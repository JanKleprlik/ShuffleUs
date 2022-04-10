package com.shuffleus.app.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.databinding.LectureItemBinding

class LecturesAdapter(lectures: List<Lecture>): RecyclerView.Adapter<LectureViewHolder>(){

    // MVVM
    private var _binding: LectureItemBinding? = null
    private val binding: LectureItemBinding
        get() = _binding!!

    private var lectures = lectures
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        _binding = LectureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LectureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        holder.bind(lectures[position])
    }

    override fun getItemCount(): Int {
        return lectures.count()
    }
}

class LectureViewHolder(binding: LectureItemBinding): RecyclerView.ViewHolder(binding.root){

    var tvLectureCode: TextView = binding.tvLectureCode
    var tvLectureLecturer: TextView = binding.tvLectureLecturer
    var tvLectureRoom: TextView = binding.tvLectureRoom
    var tvLectureSubject: TextView = binding.tvLectureSubject
    var tvLectureTime: TextView = binding.tvLectureTime


    fun bind(lecture: Lecture){
        tvLectureCode.text = lecture.code
        tvLectureLecturer.text = lecture.lecturer
        tvLectureRoom.text = lecture.room
        tvLectureSubject.text = lecture.subject
        tvLectureTime.text = lecture.startTime.toString()
    }
}