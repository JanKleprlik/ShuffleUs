package com.shuffleus.app.schedule;

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.google.android.material.tabs.TabItem
import com.shuffleus.app.R
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.data.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.shuffleus.app.utils.CallbackListener
import com.shuffleus.app.utils.LectureCallbackListener


public class AddLectureFragment (private val callbackListener: LectureCallbackListener) : DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_lecture, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }


    private fun checkHours(hours : Int) :Boolean{
        return hours in 0..23
    }

    private fun checkMinutes(minutes : Int) :Boolean{
        return minutes in 0..59
    }

    private fun checkLectureTime(startArray : List<String>,endArray: List<String>) : Boolean{
        if(startArray.size != 2 || endArray.size != 2){
            return false
        }

        try {
            val startHours = Integer.parseInt(startArray[0])
            val startMinutes = Integer.parseInt(startArray[1])
            val endHours = Integer.parseInt(endArray[0])
            val endMinutes = Integer.parseInt(endArray[1])

            if(!checkHours(startHours) || !checkMinutes(startMinutes) || !checkHours(endHours) || !checkMinutes(endMinutes)){
                return false
            }
        } catch (e: NumberFormatException) {
            return false
        }

        return true
    }

    private fun getNewLecture(view:View) : Lecture? {

        val endText = view.findViewById<EditText>(R.id.ettEnd)
        val startText = view.findViewById<EditText>(R.id.ettStart)

        val startArray : List<String> = startText.text.split(":")
        val endArray : List<String> = endText.text.split(":")
        if(!checkLectureTime(startArray,endArray)){
            return null
        }
        val lectureTeacher : String = view.findViewById<EditText>(R.id.etLecturer).text.toString()
        val lectureCode : String = view.findViewById<EditText>(R.id.etCode).text.toString()
        val lectureRoom : String = view.findViewById<EditText>(R.id.etRoom).text.toString()
        val lectureName : String = view.findViewById<EditText>(R.id.etSubject).text.toString()
        val lectureTalk : Boolean = view.findViewById<SwitchCompat>(R.id.sLection).isChecked



        val lectureStart : Int =  60 * startArray[0].toInt() + startArray[1].toInt()
        val lectureEnd : Int =  60 * endArray[0].toInt() + endArray[1].toInt()
        val tlWeekday = view.findViewById<TabLayout>(R.id.tlWeekday)

        return Lecture(tlWeekday.selectedTabPosition,lectureStart,lectureEnd,lectureName,lectureTeacher,lectureTalk,lectureRoom,lectureCode)
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addBtn = view.findViewById<Button>(R.id.btnAdd)
        addBtn.setOnClickListener{
            val lecture = getNewLecture(view)
            if(lecture == null){
                //todo error message
            }
            else {
                callbackListener.onLectureAdded(lecture)
                dismiss()
            }
        }

        val deleteBtn = view.findViewById<Button>(R.id.btnRemove)
        deleteBtn.setOnClickListener{

                callbackListener.onLecturesDeleted()
                dismiss()
        }
    }
}