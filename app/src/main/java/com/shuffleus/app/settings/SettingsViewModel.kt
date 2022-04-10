package com.shuffleus.app.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.shuffleus.app.AppSettings
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.memory.InMemoryRepository
import com.shuffleus.app.repository.room.RoomRepository
import com.shuffleus.app.utils.ViewModelResponseState
import kotlinx.coroutines.launch

class SettingsViewModel(app:Application): AndroidViewModel(app) {

    //private val repository: Repository by lazy { InMemoryRepository() }
    private val repository: Repository by lazy { RoomRepository(app.baseContext) }

    private val appSettings: AppSettings by lazy {
        AppSettings(app)
    }

    private val _allUsers : MutableLiveData<ViewModelResponseState<List<User>, String>> by lazy {
        MutableLiveData<ViewModelResponseState<List<User>, String>>().apply {
            value = ViewModelResponseState.Idle
        }
    }

    suspend fun deleteUser(user: User){
        repository.deleteUser(user)
    }

    suspend fun getGroupNamesIdx() :Int{
        return appSettings.getGroupnamesIdx()
    }

    suspend fun getGroupSize() : Int{
        return appSettings.getGroupSize()
    }

    suspend fun getTimerIdx(): Int{
        return appSettings.getTimerIdx()
    }

    fun updateTimerIdx(newIdx: Int){
        viewModelScope.launch {
            appSettings.setTimerIdx(newIdx)
        }
    }

    fun updateGroupsName(newGroupnamesIdx: Int){
        viewModelScope.launch {
            appSettings.setGroupnamesIdx(newGroupnamesIdx)
        }
    }

    fun updateGroupSize(newGroupSize: Int){
        viewModelScope.launch {
            appSettings.setGroupSize(newGroupSize)
        }
    }

    suspend fun getNumberOfRawActiveUsers(): Int {
        return repository.getRawActiveUsers().size
    }

    suspend fun getNameTypes() : List<String> {
        return repository.getGroupNames() as List<String>
    }

    fun getUsers(): LiveData<ViewModelResponseState<List<User>, String>> {
        return _allUsers
    }

    suspend fun addUser(user:User) {
        repository.addUser(user)
        //Reload users
        loadUsers()
    }

    suspend fun loadData(){
        _allUsers.postValue(ViewModelResponseState.Loading)
        loadUsers()
    }

    private suspend fun loadUsers(){
        _allUsers.postValue(ViewModelResponseState.Loading)

        val users = repository.getUsers()
        _allUsers.postValue(ViewModelResponseState.Success(users))
    }
}