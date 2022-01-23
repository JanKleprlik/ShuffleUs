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
import com.shuffleus.app.utils.ViewModelResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class MainViewModel(app:Application): AndroidViewModel(app) {

    private val repository: Repository by lazy { InMemoryRepository() }

    private val appSettings: AppSettings by lazy {
        AppSettings(app)
    }

    private val _activeUsers : MutableLiveData<ViewModelResponseState<List<User>, String>> by lazy {
        MutableLiveData<ViewModelResponseState<List<User>, String>>().apply {
            value = ViewModelResponseState.Idle
        }
    }

    fun getUsers(): LiveData<ViewModelResponseState<List<User>, String>> {
        return _activeUsers
    }

    fun getGroups(groupSize: Int): LiveData<ViewModelResponseState<List<Group>, String>>{
        val activeUsers = repository.getActiveUsers()

        val chunkedPeople = activeUsers.chunked(groupSize)
        val groups = mutableListOf<Group>()

        chunkedPeople.forEachIndexed { index, list ->
            groups.add(Group(repository.getGroupName(index, "Food"), list))
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