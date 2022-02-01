package com.shuffleus.app.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shuffleus.app.AppSettings
import com.shuffleus.app.data.Group
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.room.RoomRepository
import com.shuffleus.app.utils.ViewModelResponseState
import kotlin.random.Random

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
        return appSettings.getTimerIdx() * 5 * 60 * 1000
    }

    fun getUsers(): LiveData<ViewModelResponseState<List<User>, String>> {
        return _activeUsers
    }

    suspend fun getGroups(): LiveData<ViewModelResponseState<List<Group>, String>>{
        if (isLastRunValid() == false)
            return MutableLiveData(ViewModelResponseState.Error("Previous run is not valid. Try Shuffle."))

        val groups = mutableListOf<Group>()

        // group names (Food, Greek, ...)
        val groupName = repository.getGroupNames().elementAt(appSettings.getOldGroupnamesIdx() - 1) //-1 is there for minim value of the picker

        // active users chunked by group size
        val chunkedPlayers = getUniquelyChunkedPlayers()

        chunkedPlayers.forEachIndexed { index, list ->
            groups.add(Group(repository.getGroupName(index, groupName ), list))
        }
        return MutableLiveData(ViewModelResponseState.Success(groups))
    }

    suspend fun updatePreferences(): Unit{
        // update seed
        appSettings.incrementSeed()

        // update old values
        appSettings.updateGroupnamesIdx()
        appSettings.updateOldGroupSize()
        appSettings.updateOldSeed()

        // update active players in DB
        repository.makeUsersCurrent()
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

    private suspend fun isLastRunValid(): Boolean {
        return appSettings.getOldGroupSize() != -1 &&
                appSettings.getOldGroupnamesIdx() != -1
    }

    private suspend fun getUniquelyChunkedPlayers() :List<List<User>>{
        var activeUsers = repository.getActiveUsers()
        val groupSize = appSettings.getOldGroupSize()

        activeUsers = activeUsers.shuffled(Random(appSettings.getOldSeed()))
        return activeUsers.chunked(groupSize)
    }
}