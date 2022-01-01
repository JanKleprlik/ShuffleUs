package com.shuffleus.app.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.memory.InMemoryRepository
import com.shuffleus.app.repository.room.RoomRepository
import com.shuffleus.app.utils.ViewModelResponseState

class SettingsViewModel(context: Context): ViewModel() {

    //private val repository: Repository by lazy { InMemoryRepository() }
    private val repository: Repository by lazy { RoomRepository(context) }

    private val _allUsers : MutableLiveData<ViewModelResponseState<List<User>, String>> by lazy {
        MutableLiveData<ViewModelResponseState<List<User>, String>>().apply {
            value = ViewModelResponseState.Idle
        }
    }

    fun getNumberOfActiveUsers(): Int {
        return repository.getActiveUsers().size
    }

    fun getNameTypes() : List<String> {
        return repository.getGroupNames() as List<String>
    }

    fun getUsers(): LiveData<ViewModelResponseState<List<User>, String>> {
        return _allUsers
    }

    fun loadData(){
        _allUsers.postValue(ViewModelResponseState.Loading)
        loadUsers()
    }

    private fun loadUsers(){
        _allUsers.postValue(ViewModelResponseState.Loading)

        val users = repository.getUsers()
        _allUsers.postValue(ViewModelResponseState.Success(users))
    }
}