package com.shuffleus.app.schedule

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.shuffleus.app.R
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.databinding.FragmentAddLectureBinding
import com.shuffleus.app.utils.LectureCallbackListener
import java.io.File
import java.lang.Exception


class AddLectureFragment (private val callbackListener: LectureCallbackListener) : DialogFragment(){

    // MVVM
    private var _binding: FragmentAddLectureBinding? = null
    private val binding: FragmentAddLectureBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLectureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addBtn = binding.btnAdd
        addBtn.setOnClickListener{
            val lecture = getNewLecture()
            if(lecture == null){
                Toast.makeText(activity, "Please fill at least start and finish time", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(activity, "Lecture added", Toast.LENGTH_LONG).show()
                callbackListener.onLectureAdded(lecture)
            }
        }

        val doneBtn = binding.btnBack
        doneBtn.setOnClickListener{
            dismiss()
        }

        val deleteBtn = binding.btnRemove
        deleteBtn.setOnClickListener{
            callbackListener.onLecturesDeleted()
            dismiss()
        }

        val csvBtn = binding.btnCsv
        csvBtn.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 777)
        }
    }

    // Here we get result of user interaction - user finds CSV file
    // we extract information about lectures and add them
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 777) {
            var fileName = data?.data?.path
            fileName = fileName!!.split(":")[1]
            try {
                val lines: MutableList<String> = File(fileName).readLines(Charsets.ISO_8859_1) as MutableList<String>
                lines.removeFirst()
                lines.forEach { line -> addLectureFromCsvLine(line) }
            }
            catch(ex:Exception){
                Log.e("CSV",ex.message.toString())
            }
        }
    }

    // Interpreting single line of CSV file as one lecture information
    private fun addLectureFromCsvLine(line :String){
        val tokens : List<String> =  line.split(";")
        callbackListener.onLectureAdded(Lecture(
            tokens[4].toInt()-1,
            tokens[5].toInt(),
            tokens[5].toInt()+ tokens[7].toInt(),
            tokens[3],
            tokens[12],
            tokens[1][tokens[1].length-2]=='p',
            tokens[6],
            tokens[2],
        ))
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

    //Gets user input from provided views and builds lecture. Only start and end times are required
    private fun getNewLecture() : Lecture? {
        val endText = binding.ettEnd
        val startText = binding.ettStart

        val startArray : List<String> = startText.text.split(":")
        val endArray : List<String> = endText.text.split(":")

        if(!checkLectureTime(startArray,endArray)){
            return null
        }

        val lectureTeacher = binding.etLecturer
        val lectureCode = binding.etCode
        val lectureRoom= binding.etRoom
        val lectureName = binding.etSubject
        val lectureTalk = binding.sLection

        val lectureStart : Int =  60 * startArray[0].toInt() + startArray[1].toInt()
        val lectureEnd : Int =  60 * endArray[0].toInt() + endArray[1].toInt()
        val tlWeekday = binding.tlWeekday

        val lec = Lecture(
            tlWeekday.selectedItemPosition,
            lectureStart,
            lectureEnd,
            lectureName.text.toString(),
            lectureTeacher.text.toString(),
            lectureTalk.isChecked,
            lectureRoom.text.toString(),
            lectureCode.text.toString())

        endText.text.clear()
        startText.text.clear()
        lectureName.text.clear()
        lectureCode.text.clear()
        lectureTeacher.text.clear()
        lectureRoom.text.clear()
        return lec
    }

}