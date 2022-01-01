package com.shuffleus.app.repository.memory

import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository

class InMemoryRepository :Repository {

    override fun getUsers(): List<User> {
        return listOf(
            User(name="Jan", surname = "Kleprlík", isActive = true ),
            User(name="Michal", surname = "Fuleky", isActive = true),
            User(name="Marek", surname = "Majer", isActive = true),
            User(name="Michal", surname = "Ivicic", isActive = false),
            User(name="Ondřej", surname = "Müller", isActive = false),
            User(name="Eliška", surname = "Suchardová", isActive = true),
            User(name="Pavel", surname = "Zajíc", isActive = false),
        )


    }

    override fun getActiveUsers(): List<User> {
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

    override fun getGroupNames(): List<String>{
        return listOf(
            "Greek",
            "Food",
            "Drinks",
        )
    }

    override fun getGroupName(index: Int, groupName: String): String{
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