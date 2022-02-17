package com.shuffleus.app.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.shuffleus.app.R
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.repository.LectureRepository
import com.shuffleus.app.repository.room.LectureRoomRepository
import com.shuffleus.app.repository.room.RoomRepository

class LecturesAdapter(lectures: List<Lecture>): RecyclerView.Adapter<LectureViewHolder>(){

    var lectures = lectures
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lecture_item, parent, false)

        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        holder.bind(lectures[position], this)
    }

    override fun getItemCount(): Int {
        return lectures.count()
    }
}

class LectureViewHolder(private val view: View): RecyclerView.ViewHolder(view){
    //private val repository: LectureRepository by lazy { LectureRoomRepository(view.context) }

    var tvLectureCode: TextView = view.findViewById(R.id.tvLectureCode)
    var tvLectureLecturer: TextView = view.findViewById(R.id.tvLectureLecturer)
    var tvLectureRoom: TextView = view.findViewById(R.id.tvLectureRoom)
    var tvLectureSubject: TextView = view.findViewById(R.id.tvLectureSubject)
    var tvLectureTime: TextView = view.findViewById(R.id.tvLectureTime)


    fun bind(lecture: Lecture, adapter: LecturesAdapter){
        tvLectureCode.text = lecture.code
        tvLectureLecturer.text = lecture.lecturer
        tvLectureRoom.text = lecture.room
        tvLectureSubject.text = lecture.subject
        tvLectureTime.text = lecture.startTime.toString()
    }
}