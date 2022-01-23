package com.shuffleus.app.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shuffleus.app.AppSettings
import com.shuffleus.app.data.Group
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.memory.InMemoryRepository
import com.shuffleus.app.repository.room.RoomRepository
import com.shuffleus.app.utils.ViewModelResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext

class MainViewModel(app:Application): AndroidViewModel(app) {

    //private val repository: Repository by lazy { InMemoryRepository() }
    private val repository: Repository by lazy { RoomRepository(app.baseContext) }

    private val appSettings: AppSettings by lazy {
        AppSettings(app)
    }

    private val _activeUsers : MutableLiveData<ViewModelResponseState<List<User>, String>> by lazy {
        MutableLiveData<ViewModelResponseState<List<User>, String>>().apply {
            value = ViewModelResponseState.Idle
        }
    }

    suspend fun getTimeInMillis(): Int{
        // idx * 5 minutes * 60 seconds * 1000 ms
        return 10 * 1000
        //return appSettings.getTimerIdx() * 5 * 60 * 1000
    }

    fun getUsers(): LiveData<ViewModelResponseState<List<User>, String>> {
        return _activeUsers
    }

    suspend fun getGroups(): LiveData<ViewModelResponseState<List<Group>, String>>{
        val activeUsers = repository.getActiveUsers()

        val groups = mutableListOf<Group>()

        val groupSize = appSettings.getGroupSize()
        val groupName = repository.getGroupNames().elementAt(appSettings.getGroupnamesIdx() - 1) //-1 is there for minim value of the picker
        val chunkedPeople = activeUsers.chunked(groupSize)

        chunkedPeople.forEachIndexed { index, list ->
            groups.add(Group(repository.getGroupName(index, groupName ), list))
        }
        return MutableLiveData(ViewModelResponseState.Success(groups))
    }

    fun loadData(){
        _activeUsers.postValue(ViewModelResponseState.Loading)
        loadUsers()
    }

    private fun loadUsers(){
        _activeUsers.postValue(ViewModelResponseState.Loading)

        val users = repository.getActiveUsers()
        _activeUsers.postValue(ViewModelResponseState.Success(users))
    }
}