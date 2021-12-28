package com.shuffleus.app.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.memory.InMemoryRepository
import com.shuffleus.app.utils.ViewModelResponseState

class MainViewModel: ViewModel() {

    private val repository: Repository by lazy { InMemoryRepository() }

    private val _users : MutableLiveData<ViewModelResponseState<List<User>, String>> by lazy {
        MutableLiveData<ViewModelResponseState<List<User>, String>>().apply {
            value = ViewModelResponseState.Idle
        }
    }

    fun getUsers(): LiveData<ViewModelResponseState<List<User>, String>> {
        return _users
    }

    fun loadData(){
        _users.postValue(ViewModelResponseState.Loading)
        loadUsers()
    }

    private fun loadUsers(){
        _users.postValue(ViewModelResponseState.Loading)

        val users = repository.getUsers()
        _users.postValue(ViewModelResponseState.Success(users))
    }
}