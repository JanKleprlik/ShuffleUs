package com.shuffleus.app.schedule

import android.app.Application
import androidx.lifecycle.*
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.repository.LectureRepository
import com.shuffleus.app.repository.room.LectureRoomRepository
import com.shuffleus.app.utils.ViewModelResponseState

class ScheduleViewModel(app:Application): AndroidViewModel(app) {
    //private val repository: Repository by lazy { InMemoryLectureRepository() }
    private val repository: LectureRepository by lazy { LectureRoomRepository(app.baseContext) }

    private val _allLectures : MutableLiveData<ViewModelResponseState<List<Lecture>, String>> by lazy {
        MutableLiveData<ViewModelResponseState<List<Lecture>, String>>().apply {
            value = ViewModelResponseState.Idle
        }
    }


    fun getLectures(): LiveData<ViewModelResponseState<List<Lecture>, String>> {
        return _allLectures
    }

    suspend fun addLecture(lecture:Lecture) {
        repository.addLecture(lecture)
        //Reload lectures
        loadLectures()
    }

    suspend fun deleteLectures(){
        repository.run { deleteLectures() }
        //Reload lectures
        loadLectures()
    }

    suspend fun loadData(){
        _allLectures.postValue(ViewModelResponseState.Loading)
        loadLectures()
    }

    private suspend fun loadLectures(){
        _allLectures.postValue(ViewModelResponseState.Loading)

        val lectures = repository.getLectures()
        //@todo error if null
        _allLectures.postValue(ViewModelResponseState.Success(lectures))
    }
}
