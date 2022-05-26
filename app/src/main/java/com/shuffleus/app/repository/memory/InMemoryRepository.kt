package com.shuffleus.app.repository.memory

import com.shuffleus.app.data.Group
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository

class InMemoryRepository :Repository {

    override suspend fun getUsers(): List<User> {
        return listOf(
            User(name="Jan", surname = "Kleprlík", isActive = true),
            User(name="Michal", surname = "Fuleky", isActive = true),
            User(name="Marek", surname = "Majer", isActive = true),
            User(name="Michal", surname = "Ivicic", isActive = false),
            User(name="Ondřej", surname = "Müller", isActive = false),
            User(name="Eliška", surname = "Suchardová", isActive = true),
            User(name="Pavel", surname = "Zajíc", isActive = false),
        )


    }

    override suspend fun getActiveUsers(): List<User> {
        return listOf(
            User(name="Jan", surname = "Kleprlík", isActive = true ),
            User(name="Michal", surname = "Fuleky", isActive = true),
            User(name="Marek", surname = "Majer", isActive = true),
            User(name="Eliška", surname = "Suchardová", isActive = true),
            User(name="Eliška", surname = "Suchardová", isActive = true),
            User(name="Eliška", surname = "Suchardová", isActive = true),
            User(name="Eliška", surname = "Suchardová", isActive = true),
            User(name="Eliška", surname = "Suchardová", isActive = true),
        )
    }

    override suspend fun getRawActiveUsers(): List<User> {
        throw NotImplementedError("Operation: getRawActiveUsers is not implemented on MemoryRepository.")

    }

    override suspend fun getGroupNames(): List<String>{
        return listOf(
            "Greek",
            "Food",
            "Drinks",
        )
    }

    override suspend fun updateUser(user: User) {
        throw NotImplementedError("Operation: updateUser is not implemented on MemoryRepository.")
    }

    override suspend fun deleteUser(user: User) {
        throw NotImplementedError("Operation: deleteUser is not implemented on MemoryRepository.")
    }

    override suspend fun getGroupNamesAll(): Iterable<GroupNames> {
        throw NotImplementedError("Operation: getGroupNamesAll is not implemented on MemoryRepository.")
    }

    override suspend fun addUser(vararg users: User): Unit =
        throw NotImplementedError("Operation: addUser is not implemented on MemoryRepository.")

    override suspend fun makeUsersCurrent() {
        throw NotImplementedError("Operation: makeUsersCurrent is not implemented on MemoryRepository.")
    }

    override suspend fun getGroupName(index: Int, groupName: String): String{
        val names = listOf<String>(
            "Alpha",
            "Beta",
            "Gamma",
            "Delta",
            "Epsilon",
            "Zeta",
            "Eta",
            "Theta",
        )

        return names[index]
    }
}